package org.example.userservice.dto;

public record UserResponse(Long id,
                           String userName,
                           String firstName,
                           String lastName,
                           String email) {
}
