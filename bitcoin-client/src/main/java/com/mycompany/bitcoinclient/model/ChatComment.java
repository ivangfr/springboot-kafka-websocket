package com.mycompany.bitcoinclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class ChatComment {

    private String username;
    private String message;
    private Date timestamp;

}
