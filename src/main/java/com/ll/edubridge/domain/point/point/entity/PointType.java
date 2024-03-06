package com.ll.edubridge.domain.point.point.entity;

import lombok.Getter;

@Getter
public enum PointType {

    Attend("출석", 500),
    SNote("요약 노트 작성", 700),
    ;

    private final String content;
    private final int amount;

    PointType(String content, int amount) {
        this.content = content;
        this.amount = amount;
    }
}
