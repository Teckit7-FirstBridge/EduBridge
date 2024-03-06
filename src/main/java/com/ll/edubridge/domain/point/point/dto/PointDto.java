package com.ll.edubridge.domain.point.point.dto;

import com.ll.edubridge.domain.point.point.entity.Point;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
public class PointDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String content;
    @NonNull
    private Long ownerId;
    @NonNull
    private int amount;
    @NonNull
    private int totalAmount;

    public PointDto(Point point, Member member) {
        this.id = point.getId();
        this.createDate = point.getCreateDate();
        this.content = point.getContent();
        this.ownerId = point.getOwnerId();
        this.amount = point.getAmount();
    }
}
