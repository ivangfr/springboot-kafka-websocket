package com.ivanfranchin.bitcoinclient.model;

import java.time.LocalDateTime;

public record ChatComment(String fromUser, String message, LocalDateTime timestamp) {
}
