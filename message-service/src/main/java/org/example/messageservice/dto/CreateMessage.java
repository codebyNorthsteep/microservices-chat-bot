package org.example.messageservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMessage(
        @NotBlank(message = "Username cannot be blank")
        String username,
        @NotBlank(message = "Content cannot be blank")
        @Size(max = 1000, message = "Message cannot exceed 1000 characters")
        String content
) {}
