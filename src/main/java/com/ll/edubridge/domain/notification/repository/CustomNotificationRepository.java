package com.ll.edubridge.domain.notification.repository;

import com.ll.edubridge.domain.notification.entity.Notification;

import java.util.List;

public interface CustomNotificationRepository {
    List<Notification> findByMemberId(Long id);
}
