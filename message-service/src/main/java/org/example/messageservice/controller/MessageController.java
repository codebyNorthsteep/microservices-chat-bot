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

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("/messages")
    public ResponseEntity<ReceiveMessage> createMessage(@Valid @RequestBody CreateMessage messageRequest) {
        ReceiveMessage receiveMessage = messageService.saveMessage(messageRequest);
        log.info("Message created with id: {}", receiveMessage.id());
        return ResponseEntity.created(URI.create("/api/messages/" + receiveMessage.username())).body(receiveMessage);
    }

    @GetMapping("/messages/{username}")
    public ResponseEntity<List<ReceiveMessage>> getMessagesByUsername(@PathVariable String username) {
        List<ReceiveMessage> messages = messageService.getAllMessages(username);
        log.info("Messages retrieved a user");
        return ResponseEntity.ok(messages);
    }
}
