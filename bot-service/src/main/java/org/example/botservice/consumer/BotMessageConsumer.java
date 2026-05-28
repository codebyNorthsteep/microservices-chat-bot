package org.example.botservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.botservice.dto.MessageEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class BotMessageConsumer {
    //Anropa
    private final RestClient messageClient = RestClient.create("http://localhost:8081");

    @RabbitListener(queues = "message-published")
    public void receiveMessageEvent(MessageEvent messageEvent) {
        log.info("Received message event from: {}", messageEvent.username());

        //ignore bot answers to not get in to a loop by sending bot-answers to message-queue
        if (messageEvent.username().equals("Bot")) {
            log.info("Ignoring message from Bot to avoid loop");
            return;
        }

        String reply = generateBotReply(messageEvent.content());

        messageClient.post()
                .uri("/api/messages")
                .header("X-User-Name", "Bot")
                .body(new BotReply(reply))
                .retrieve()
                .toBodilessEntity();

        log.info("Bot replied: {}", reply);

    }

    private String generateBotReply(String userMessage) {
        String lowerCaseMsg = userMessage.toLowerCase();

        //triggers
        if (lowerCaseMsg.contains("hello") || lowerCaseMsg.contains("hi")) {
            return "Hello there! 👋";
        }
        if (lowerCaseMsg.contains("coffee")) {
            return "Coffee? ☕️ I only drink electricity, but I fully support your caffeine addiction!";
        }
        if (lowerCaseMsg.contains("joke")) {
            return "A SQL query goes into a bar, walks up to two tables and asks... 'Can I join you?' 🍻";
        }
        if (lowerCaseMsg.contains("help")) {
            return "Help is on the way dear! Well, actually, I'm just a bot... so you're probably on your own. 🚑";
        }

        // Random default reply for if nothing specific was triggered
        int randomIndex = ThreadLocalRandom.current().nextInt(defaultReplies.size());
        return defaultReplies.get(randomIndex);
    }

    private final List<String> defaultReplies = List.of(
            "Interesting... tell me more! 🍿",
            "I'm just a bot, but I feel strongly about that.",
            "Let me think about it. 🤖 ... No.",
            "42. The answer to everything is 42.",
            "Have you tried turning it off and on again?",
            "That sounds like a problem for tomorrow's me."
    );

    record BotReply(String content) {}

}
