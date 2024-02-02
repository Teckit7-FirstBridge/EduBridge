package com.ll.edubridge.domain.openai;

import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.service.SummaryNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {
    
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${openai.model}")
    private String model;
    
    @Value("${openai.api.url}")
    private String apiUrl;


    @Autowired
    SummaryNoteService summaryNoteService;

    @Async
    @Transactional
    public void chat(Long summaryNoteId,String prompt) {
        // create a request
        System.out.println("---------helo-------");
        ChatRequest request = new ChatRequest(model, prompt);
        SummaryNote summaryNote = summaryNoteService.getSummaryNote(summaryNoteId);
        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {

        }else{
            System.out.println("---------chanwoo------------");
            System.out.println(response.getChoices().get(0).getMessage().getContent());
            String numberOnly = response.getChoices().get(0).getMessage().getContent().replaceAll("[^\\d]", "");
            Long score = Long.parseLong((numberOnly));
            summaryNote.setScore(score);
            System.out.println(score);
        }

        // return the first response
    }
}