package org.example.messageservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageEvent(UUID eventId,
                           String username,
                           String content,
                           LocalDateTime sentAt) {
}
