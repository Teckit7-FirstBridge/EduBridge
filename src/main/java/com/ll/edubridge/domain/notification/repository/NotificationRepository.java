package com.ll.edubridge.domain.notification.repository;

import com.ll.edubridge.domain.notification.entity.Notification;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationRepository extends JpaRepository<Notification, Long>, CustomNotificationRepository {
    Notification findByComment(Comment comment);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.comment = :comment")
    void deleteByComment(@Param("comment") Comment comment);
}
