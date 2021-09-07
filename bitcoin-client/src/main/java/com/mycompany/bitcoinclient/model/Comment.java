package com.mycompany.bitcoinclient.model;

import lombok.Value;

@Value
public class Comment {

    String fromUser;
    String toUser;
    String message;
}
