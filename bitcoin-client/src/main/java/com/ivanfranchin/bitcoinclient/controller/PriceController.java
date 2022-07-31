package com.ivanfranchin.bitcoinclient.controller;

import com.ivanfranchin.bitcoinclient.model.ChatComment;
import com.ivanfranchin.bitcoinclient.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class PriceController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/")
    public String getPrices() {
        return "prices";
    }

    @MessageMapping("/chat")
    public void addChatComment(@Payload Comment comment) {
        ChatComment chatComment = new ChatComment(comment.fromUser(), comment.message(), LocalDateTime.now());
        if (comment.toUser().isEmpty()) {
            simpMessagingTemplate.convertAndSend("/topic/comments", chatComment);
        } else {
            simpMessagingTemplate.convertAndSendToUser(comment.toUser(), "/topic/comments", chatComment);
        }
    }
}
