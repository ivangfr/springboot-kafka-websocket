package com.mycompany.bitcoinclient.controller;

import com.mycompany.bitcoinclient.model.ChatComment;
import com.mycompany.bitcoinclient.model.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@Controller
public class PriceController {

    @GetMapping("/")
    public String getPrices() {
        return "prices";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/comments")
    public ChatComment addChatComment(@Valid @RequestBody Comment comment) {
        return new ChatComment(comment.getUsername(), comment.getMessage(), LocalDateTime.now());
    }

}
