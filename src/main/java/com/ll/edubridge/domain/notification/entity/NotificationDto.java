package com.ll.edubridge.domain.notification.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class NotificationDto {

    private String recipient;

    private boolean read;

    private String sender;

    private NotificationType type;

    private String post_title;
    private  int point;

    public NotificationDto(Notification notification){
          this.recipient = notification.getRecipient().getNickname();
          this.read = notification.isRead();
          this.type = notification.getType();
          this.post_title =notification.getPost() != null ? notification.getPost().getTitle() : null;
          this.point = notification.getPoint();
          this.sender = notification.getSender() != null ? notification.getSender().getNickname() : null;
    }
}
