package com.ll.edubridge.global.msg;
import lombok.Getter;

@Getter
public enum Msg {
    E200_0_CREATE_SUCCUSSED("200-0", "등록 성공"),
    E200_1_INQUIRY_SUCCUSSED("200-1","조회 성공"),
    E200_2_MODIFY_SUCCUSSED("200-2","수정 성공"),
    E200_3_DELETE_SUCCUSSED("200-3","삭제 성공"),
    E200_4_RECOMMEND_SUCCUSSED("200-4","추천 성공"),
    E200_5_CANCEL_RECOMMEND_SUCCUSSED("200-5","추천 취소 성공"),
    E200_6_SUCCUSS_LOGOUT("200-6","로그아웃 성공"),
    E200_7_SUCCUSS_CANCEL_REPORT("200-7", "신고 거절 성공"),
    E200_8_SUCCUSS_REPORT("200-8", "신고 성공"),
    E400_1_CREATE_FAILED("400-1","등록 실패");


    private final String code;

    private final String msg;

    Msg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}