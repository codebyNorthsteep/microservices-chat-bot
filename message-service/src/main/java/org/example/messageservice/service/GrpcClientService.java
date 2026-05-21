package org.example.messageservice.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.example.grpc.UserServiceGrpc;
import org.example.grpc.UserRequest;
import org.example.grpc.UserResponse;


@RestController
public class GrpcClientService {
    final UserServiceGrpc.UserServiceBlockingStub stub;

    public GrpcClientService(UserServiceGrpc.UserServiceBlockingStub stub) {
        this.stub = stub;
    }

    @GetMapping("/api/test")
    public String getUser(@RequestParam(defaultValue = "Guest") String name) {
        UserRequest request = UserRequest.newBuilder()
                .setUsername(name)
                .build();
        UserResponse response = stub.getUser(request);

        return "Hello, " + response.getUsername() + "!";
    }
}
