package com.ll.edubridge.domain.openai;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int n = 1;
    private double temperature;

    public ChatRequest(String model, String prompt) {
        this.model = model;

        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

    public ChatRequest() {
    }

    public ChatRequest(String model, List<Message> messages,double temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }
    // getters and setters
}