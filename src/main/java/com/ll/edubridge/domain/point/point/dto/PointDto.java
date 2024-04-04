package com.ll.edubridge.domain.point.point.dto;

import com.ll.edubridge.domain.point.point.entity.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
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

    public PointDto(Point point) {
        this.id = point.getId();
        this.createDate = point.getCreateDate();
        this.content = point.getContent();
        this.ownerId = point.getOwnerId();
        this.amount = point.getAmount();
    }
}
