package com.ivanfranchin.bitcoinclient.websocket;

import java.time.Instant;

public record ChatMessage(String fromUser, String toUser, String comment, Instant timestamp) {
}
