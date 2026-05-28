package org.example.botservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageEvent(UUID eventId,
                           String username,
                           String content,
                           LocalDateTime sentAt) {
}
