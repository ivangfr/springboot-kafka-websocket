package com.ivanfranchin.bitcoinclient.controller;

import com.ivanfranchin.bitcoinclient.websocket.ChatMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UIControllerChatTests {

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private UIController uiController;

    @Test
    void testAddChatCommentBroadcastsWhenToUserIsEmpty() {
        ChatMessage chatMessage = new ChatMessage("user1", "", "hello everyone", Instant.now());

        uiController.addChatComment(chatMessage);

        then(simpMessagingTemplate).should().convertAndSend(
                eq("/topic/chat-messages"), eq(chatMessage));
    }

    @Test
    void testAddChatCommentSendsPrivateMessageWhenToUserIsSet() {
        ChatMessage chatMessage = new ChatMessage("user1", "user2", "hello user2", Instant.now());

        uiController.addChatComment(chatMessage);

        then(simpMessagingTemplate).should().convertAndSendToUser(
                eq("user2"), eq("/topic/chat-messages"), eq(chatMessage));
    }
}
