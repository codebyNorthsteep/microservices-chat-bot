package org.example.userservice.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import jakarta.persistence.EntityNotFoundException;
import org.example.userservice.dto.UserDto;
import org.springframework.grpc.server.service.GrpcService;
import org.example.grpc.UserServiceGrpc;
import org.example.grpc.UserRequest;
import org.example.grpc.UserResponse;


@GrpcService
public class GrpcServerService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    public GrpcServerService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {


        try {
            String username = request.getUsername();
            if (username == null || username.isBlank()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("username must be provided")
                                .asRuntimeException()
                );
                return;
            }

            UserDto userResponse = userService.getUserByUsername(username);
            responseObserver.onNext(
                    UserResponse.newBuilder()
                            .setUsername(userResponse.username())
                            .build()
            );
            responseObserver.onCompleted();
        } catch (EntityNotFoundException ex) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("User not found").asRuntimeException()
            );
        } catch (Exception ex) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Failed to fetch user").asRuntimeException()
            );
        }
    }
}
