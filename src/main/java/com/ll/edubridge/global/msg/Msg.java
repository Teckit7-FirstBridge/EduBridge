package com.ll.edubridge.global.msg;
import lombok.Getter;

@Getter
public enum Msg {
    CREATE("등록 성공"),
    MODIFY("수정 성공"),
    DELETE("삭제 성공"),
    CHECK("조회 성공"),
    RECOMMEND("추천 성공"),
    RECOMMENDCANCEL("추천 취소 성공"),
    CREATEFAILURE("등록 실패"),
    MODIFYFAILURE("수정 실패"),
    DELETEFAILURE("삭제 실패"),
    CHECKFAILURE("조회 실패");

    private final String msg;

    Msg(String msg) {
        this.msg = msg;
    }
}