package com.ll.edubridge.domain.notification.controller;

import com.ll.edubridge.domain.member.member.repository.MemberRepository;
import com.ll.edubridge.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    // 1. 모든 Emitters를 저장하는 ConcurrentHashMap

    // 메시지 알림
    @GetMapping("/api/notification/subscribe")
    public SseEmitter subscribe(Long id) {
        SseEmitter sseEmitter = notificationService.subscribe(id);

        return sseEmitter;
    }
}