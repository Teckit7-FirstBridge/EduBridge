package com.ll.edubridge.domain.openai;

import lombok.Data;

import java.util.List;
@Data
public class ChatResponse {

    private List<Choice> choices;

    public ChatResponse(List<Choice> choices) {
        this.choices = choices;
    }

    public ChatResponse() {}

    @Data
    public static class Choice {
        private int index;
        private Message message;

        public Choice(int index, Message message) {
            this.index = index;
            this.message = message;
        }

        public Choice() {}
    }
}