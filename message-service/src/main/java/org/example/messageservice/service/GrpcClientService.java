package org.example.messageservice.service;

import org.springframework.stereotype.Service;

import org.example.grpc.UserServiceGrpc;
import org.example.grpc.UserRequest;
import org.example.grpc.UserResponse;


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
        UserResponse response = stub.getUser(request);

        return response.getUsername();
    }
}
