package org.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@NotBlank(message = "Username cannot be blank")
                                String username,
                                @NotBlank(message = "First name cannot be blank")
                                @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
                                String firstName,
                                @NotBlank(message = "Last name cannot be blank")
                                @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
                                String lastName,
                                @Email(message = "Email should be valid")
                                @NotBlank(message = "Email cannot be blank")
                                String email,
                                @NotBlank(message = "Password cannot be blank")
                                String password) {
}
