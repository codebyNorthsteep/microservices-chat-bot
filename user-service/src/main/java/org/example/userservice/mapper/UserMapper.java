package org.example.userservice.mapper;

import org.example.userservice.dto.CreateUserRequest;
import org.example.userservice.dto.UpdateUserRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        User entity = new User();
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setEmail(request.email());
        entity.setPassword(request.password());
        entity.setUsername(request.username());
        return entity;

    }

    public void updateEntity(UpdateUserRequest request, User entity) {
        if (request == null || entity == null) return;

        entity.setUsername(request.username());
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setEmail(request.email());
    }

    public UserResponse toUserResponse(User entity){
        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail());

    }
}

