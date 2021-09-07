package com.mycompany.bitcoinclient.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value(staticConstructor = "of")
public class ChatComment {

    String fromUser;
    String message;
    LocalDateTime timestamp;
}
