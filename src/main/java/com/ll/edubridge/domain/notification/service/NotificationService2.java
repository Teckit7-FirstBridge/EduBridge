package com.ll.edubridge.domain.notification.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.notification.entity.Notification;
import com.ll.edubridge.domain.notification.entity.NotificationType;
import com.ll.edubridge.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService2 {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void create(NotificationType type,Member member,String content) {

        Notification notification = Notification.builder()
                .type(type)
                .recipient(member)
                .content(content)
                .build();

        notificationRepository.save(notification);
    }


}
