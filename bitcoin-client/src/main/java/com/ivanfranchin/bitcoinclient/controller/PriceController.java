package com.ivanfranchin.bitcoinclient.controller;

import com.ivanfranchin.bitcoinclient.websocket.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class PriceController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/")
    public String getPrices() {
        return "prices";
    }

    @MessageMapping("/chat")
    public void addChatComment(@Payload ChatMessage chatMessage) {
        if (chatMessage.toUser().isEmpty()) {
            simpMessagingTemplate.convertAndSend("/topic/chat-messages", chatMessage);
        } else {
            simpMessagingTemplate.convertAndSendToUser(chatMessage.toUser(), "/topic/chat-messages", chatMessage);
        }
    }
}
