package com.mycompany.bitcoinclient.model;

import lombok.Data;

@Data
public class Comment {

    private String fromUser;
    private String toUser;
    private String message;

}
