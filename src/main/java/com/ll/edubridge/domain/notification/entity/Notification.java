package com.ll.edubridge.domain.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class Notification extends BaseEntity {



    private Long sender_id;

    private Long recipient_id;

    private boolean read;


    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @JsonIgnore
    @ManyToOne
    private Post post;

    private  int point = 0;

    @JsonIgnore
    @OneToOne
    private Comment comment;
}
