package org.example.userservice.dto;

public record UserDto(Long id,
                      String username,
                      String firstName,
                      String lastName,
                      String email) {
}
