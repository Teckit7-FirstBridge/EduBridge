package com.ll.edubridge.domain.notification.repository;

import com.ll.edubridge.domain.notification.entity.Notification;

import java.util.List;

public interface CustomNotificationRepository {
    public List<Notification> findByMemberId(Long id);

}
