package com.ll.edubridge.domain.point.point.dto;

import com.ll.edubridge.domain.point.point.entity.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class AttendDto {

    @NonNull
    private LocalDateTime createDate;

    public AttendDto(Point point){
        this.createDate = point.getCreateDate();
    }
}
