package com.ll.edubridge.global.msg;
import lombok.Getter;

@Getter
public enum Msg {
    E200_0_CREATE_SUCCEED("200-0", "등록 성공"),
    E200_1_INQUIRY_SUCCEED("200-1","조회 성공"),
    E200_2_MODIFY_SUCCEED("200-2","수정 성공"),
    E200_3_DELETE_SUCCEED("200-3","삭제 성공"),
    E200_4_RECOMMEND_SUCCEED("200-4","추천 성공"),
    E200_5_CANCEL_RECOMMEND_SUCCEED("200-5","추천 취소 성공"),
    E200_6_LOGOUT_SUCCEED("200-6","로그아웃 성공"),
    E200_7_CANCEL_REPORT_SUCCEED("200-7", "신고 거절 성공"),
    E200_8_REPORT_SUCCEED("200-8", "신고 성공");


    private final String code;

    private final String msg;

    Msg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}