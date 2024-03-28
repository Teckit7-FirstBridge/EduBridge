package com.ll.edubridge.domain.notification.entity;

import lombok.Builder;
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

    public NotificationDto(Notification notification,String recipient,String sender){
          this.id =notification.getId();
          this.recipient = recipient;
          this.read = notification.isRead();
          this.type = notification.getType();
          this.post_title =notification.getPost() != null ? notification.getPost().getTitle() : null;
          this.point = notification.getPoint();
          this.sender = sender;
          this.post_id = notification.getPost()!=null? notification.getPost().getId():null;
          this.comment_id = notification.getComment() !=null?notification.getComment().getId(): null;
    }
}
