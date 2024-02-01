package com.ll.edubridge.domain.openaiv2;

import lombok.Data;
import lombok.NoArgsConstructor;

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