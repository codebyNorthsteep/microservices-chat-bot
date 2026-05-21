package org.example.messageservice.dto;

import java.time.LocalDateTime;

public record CreateMessage(String username,
                            String content) {
}
