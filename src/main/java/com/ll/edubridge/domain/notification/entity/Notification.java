package com.ll.edubridge.domain.notification.entity;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class Notification extends BaseEntity {



    @ManyToOne
    private Member recipient;

    private boolean read;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type;



}
