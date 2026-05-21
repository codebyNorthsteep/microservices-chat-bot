package org.example.messageservice.mapper;

import org.example.messageservice.dto.CreateMessage;
import org.example.messageservice.dto.ReceiveMessage;
import org.example.messageservice.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public Message toEntity(CreateMessage request) {
        Message entity = new Message();
        entity.setUsername(request.username());
        entity.setContent(request.content());
        return entity;

    }

    public ReceiveMessage toUserResponse(Message entity){
        return new ReceiveMessage(
                entity.getId(),
                entity.getUsername(),
                entity.getContent(),
                entity.getCreatedAt());

    }
}
