package com.mycompany.bitcoinclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ChatComment {

    private String fromUser;
    private String message;
    private LocalDateTime timestamp;

}
