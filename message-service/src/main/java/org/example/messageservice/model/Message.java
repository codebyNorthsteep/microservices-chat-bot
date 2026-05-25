package org.example.messageservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", unique = true, nullable = false, updatable = false)
    private UUID eventId;

    @Column(nullable = false)
    private String username;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.eventId = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }
}
