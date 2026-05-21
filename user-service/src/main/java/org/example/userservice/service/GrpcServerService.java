package org.example.userservice.service;

import io.grpc.stub.StreamObserver;

import org.example.userservice.dto.UserDto;
import org.springframework.grpc.server.service.GrpcService;
import org.example.grpc.UserServiceGrpc;
import org.example.grpc.UserRequest;
import org.example.grpc.UserResponse;


@GrpcService
public class GrpcServerService extends UserServiceGrpc.UserServiceImplBase{

    private final UserService userService;

    public GrpcServerService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();

        UserDto userResponse = userService.getUserByUsername(username);

        UserResponse response = UserResponse.newBuilder()
                .setUsername(userResponse.username())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
