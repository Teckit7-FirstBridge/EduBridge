package com.ll.edubridge.domain.openai;

import lombok.Data;

import java.util.List;
@Data
public class ChatResponse {

    private List<Choice> choices;

    public ChatResponse() {
    }

    public ChatResponse(List<Choice> choices) {
        this.choices = choices;
    }
// constructors, getters and setters
    @Data
    public static class Choice {
        public Choice() {
        }

        public Choice(int index, Message message) {
            this.index = index;
            this.message = message;
        }

        private int index;
        private Message message;

        // constructors, getters and setters
    }
}