package org.example.messageservice.dto;

import java.time.LocalDateTime;

public record ReceiveMessage(Long id,
                             String username,
                             String content,
                             LocalDateTime createdAt) {
}
