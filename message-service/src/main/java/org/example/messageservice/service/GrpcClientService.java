package org.example.messageservice.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;

import org.example.grpc.UserServiceGrpc;
import org.example.grpc.UserRequest;
import org.example.grpc.UserResponse;

import java.util.concurrent.TimeUnit;


@Service
public class GrpcClientService {
    final UserServiceGrpc.UserServiceBlockingStub stub;

    public GrpcClientService(UserServiceGrpc.UserServiceBlockingStub stub) {
        this.stub = stub;
    }

    public String getUser(String username) {
        UserRequest request = UserRequest.newBuilder()
                .setUsername(username)
                .build();
        //Add try-catch for when user is not found or gRPC call fails
        try {
            UserResponse response = stub
                    .withDeadlineAfter(1, TimeUnit.SECONDS)
                    .getUser(request);
            return response.getUsername();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new IllegalArgumentException("User not found: " + username, ex);
            }
            throw new IllegalStateException("user-service gRPC call failed", ex);
        }
    }
}
