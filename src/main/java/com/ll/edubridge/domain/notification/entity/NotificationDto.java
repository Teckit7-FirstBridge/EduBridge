package com.ll.edubridge.domain.notification.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class NotificationDto {

    private Long id;
    private String recipient;

    private boolean read;

    private String sender;

    private NotificationType type;

    private String post_title;

    private Long post_id;
    private  int point;
    private Long comment_id;

    public NotificationDto(Notification notification){
          this.id =notification.getId();
          this.recipient = notification.getRecipient().getNickname();
          this.read = notification.isRead();
          this.type = notification.getType();
          this.post_title =notification.getPost() != null ? notification.getPost().getTitle() : null;
          this.point = notification.getPoint();
          this.sender = notification.getSender() != null ? notification.getSender().getNickname() : null;
          this.post_id = notification.getPost()!=null? notification.getPost().getId():null;
          this.comment_id = notification.getComment() !=null?notification.getComment().getId(): null;
    }
}
