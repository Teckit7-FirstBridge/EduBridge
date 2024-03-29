package com.ll.edubridge.domain.openai;

import lombok.Data;

@Data
public class Message {

    private String role;
    private String content;

    public Message() {
    }

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }
    // constructor, getters and setters
}