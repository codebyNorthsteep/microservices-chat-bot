package org.example.messageservice.mapper;

import org.example.messageservice.dto.CreateMessage;
import org.example.messageservice.dto.ReceiveMessage;
import org.example.messageservice.model.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {
    public Message toEntity(CreateMessage request) {
        Message entity = new Message();
        entity.setUsername(request.username());
        entity.setContent(request.content());
        return entity;

    }

    public ReceiveMessage toReceiveMessage(Message entity){
        return new ReceiveMessage(
                entity.getId(),
                entity.getUsername(),
                entity.getContent(),
                entity.getCreatedAt());

    }

    public List<ReceiveMessage> toReceiveMessageList(List<Message> messages) {
        return messages.stream()
                .map(this::toReceiveMessage)
                .collect(Collectors.toList());
    }
}
