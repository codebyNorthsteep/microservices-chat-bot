package org.example.messageservice.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.messageservice.dto.CreateMessage;
import org.example.messageservice.dto.ReceiveMessage;
import org.example.messageservice.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages")
    public ResponseEntity<ReceiveMessage> createMessage(
            @RequestHeader("X-User-Name") String authenticatedUser,
            @Valid @RequestBody CreateMessage messageRequest) {
        //Skicka med verifierat namn till service
        ReceiveMessage receiveMessage = messageService.saveMessage(authenticatedUser, messageRequest);
        log.info("Message created with id: {}", receiveMessage.id());
        return ResponseEntity.created(URI.create("/api/messages/" + receiveMessage.id())).body(receiveMessage);
    }

    @GetMapping("/messages/me")
    public ResponseEntity<List<ReceiveMessage>> getMessagesByUsername(@RequestHeader("X-User-Name") String authenticatedUser) {
        List<ReceiveMessage> messages = messageService.getAllMessages(authenticatedUser);
        log.info("Messages retrieved a user");
        return ResponseEntity.ok(messages);
    }
}
