package com.ll.edubridge.domain.point.point.entity;

import lombok.Getter;

@Getter
public enum PointType {

    // content 글자 수 가능한 통일할 것 (프론트 때문에)
    Welecome("가입 축하", 2000),
    Attend("출석 체크", 500),
    SNote("요약 노트", 700),
    Enroll("수강 등록", 0);

    private final String content;
    private final int amount;

    PointType(String content, int amount) {
        this.content = content;
        this.amount = amount;
    }
}
