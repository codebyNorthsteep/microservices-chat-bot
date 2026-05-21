package org.example.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.example.messageservice.dto.CreateMessage;
import org.example.messageservice.dto.ReceiveMessage;
import org.example.messageservice.mapper.MessageMapper;
import org.example.messageservice.model.Message;
import org.example.messageservice.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MessageService {

private final MessageRepository messageRepository;
private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public ReceiveMessage saveMessage(CreateMessage messageRequest) {
        if (messageRequest == null) {
            throw new IllegalArgumentException("Message request cannot be null");
        }
        Message message = messageMapper.toEntity(messageRequest);
        Message savedMessage = messageRepository.save(message);
        log.info("Message created with id: {}", savedMessage.getId());
        return messageMapper.toReceiveMessage(savedMessage);
    }

    public List<ReceiveMessage> getAllMessages(String username){
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        List<Message> message = messageRepository.findByUsernameOrderByCreatedAtDesc(username);
        log.info("List of messages desc retrieved for username: {}", username);
        return messageMapper.toReceiveMessageList(message);
    }
}
