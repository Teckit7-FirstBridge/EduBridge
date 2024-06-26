package com.ll.edubridge.domain.openai;

import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.service.SummaryNoteService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.notification.entity.NotificationType;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.domain.point.point.entity.PointType;
import com.ll.edubridge.domain.point.point.service.PointService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.rq.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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

    @Autowired
    NotificationService notificationService;

    @Autowired
    Rq rq;

    @Autowired
    PointService pointService;

    @Async
    @Transactional
    public void chat(Long summaryNoteId, String prompt, Member member) {
        // create a request
        ChatRequest request = new ChatRequest(model, prompt);
        SummaryNote summaryNote = summaryNoteService.getSummaryNote(summaryNoteId);
        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        
        if(response == null || response.getChoices() == null || response.getChoices().isEmpty()) {

        } else {
            String numberOnly = response.getChoices().get(0).getMessage().getContent().replaceAll("[^\\d]", "");
            Long score = Long.parseLong((numberOnly));
            summaryNote.setScore(score);
            if(score>= AppConfig.SummaryPassScore){
                member.setDailyAchievement(member.getDailyAchievement()+1);
                int point = member.getPoint() + 700;
                member.setPoint(point);
                memberService.save(member);

                notificationService.notifySummaryNotePoint(summaryNote.getWriter().getId()); // 포인트 지급 알림
                notificationService.createByPoint(NotificationType.POINTS, summaryNote.getWriter(), PointType.SNote.getAmount()); // 알림 내역 저장
                pointService.addPoint(PointType.SNote, summaryNote.getWriter()); // 포인트 내역 추가
            }
        }
    }

    @Async
    @Transactional
    public Future<String> chat(String prompt) {
        // create a request
        ChatRequest request = new ChatRequest(model, prompt);
        // call the API
        return CompletableFuture.completedFuture(restTemplate.postForObject(apiUrl, request, ChatResponse.class).getChoices().get(0).getMessage().getContent());
    }
}