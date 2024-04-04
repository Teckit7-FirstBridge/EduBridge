package com.ll.edubridge.domain.point.point.entity;

import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Point extends BaseEntity {

    @Column(length = 200)
    private String content;

    private Long ownerId;

    private int amount;
}
