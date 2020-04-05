package com.mycompany.bitcoinclient.controller;

import com.mycompany.bitcoinclient.model.ChatComment;
import com.mycompany.bitcoinclient.model.Comment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class PriceController {

    @GetMapping("/")
    public String getPrices() {
        return "prices";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/comments")
    public ChatComment addChatComment(@Payload Comment comment) {
        return new ChatComment(comment.getUsername(), comment.getMessage(), LocalDateTime.now());
    }

}
