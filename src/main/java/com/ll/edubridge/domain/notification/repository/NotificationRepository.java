package com.ll.edubridge.domain.notification.repository;

import com.ll.edubridge.domain.notification.entity.Notification;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, CustomNotificationRepository {
    Notification findByComment(Comment comment);
}
