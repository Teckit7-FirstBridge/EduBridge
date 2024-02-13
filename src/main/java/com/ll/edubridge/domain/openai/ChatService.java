package com.ll.edubridge.domain.openai;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.service.SummaryNoteService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.rq.Rq;
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

    @Autowired
    MemberService memberService;


    @Async
    @Transactional
    public void chat(Long summaryNoteId, String prompt, Member member) {
        // create a request
        ChatRequest request = new ChatRequest(model, prompt);
        SummaryNote summaryNote = summaryNoteService.getSummaryNote(summaryNoteId);
        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {

        }else{
            String numberOnly = response.getChoices().get(0).getMessage().getContent().replaceAll("[^\\d]", "");
            Long score = Long.parseLong((numberOnly));
            summaryNote.setScore(score);
            if(score>= AppConfig.SummaryPassScore){
                member.setDailyAchievement(member.getDailyAchievement()+1);
                // 포인트 지급 -> 로컬 테스트 실패
                Course course = summaryNote.getVideo().getCourse();
                int point = member.getPoint() + 700;
                member.setPoint(point);
                memberService.save(member);
            }

        }
    }
}