package com.ll.edubridge.global.exceptions;

public enum CodeMsg {
    E403_1_NO("403-1", "권한이 없습니다."),
    E400_1_ALREADY_RECOMMENDED("400-1", "이미 추천하셨습니다"),
    E400_2_NOT_RECOMMENDED_YET("400-2", "추천을 하지 않았습니다."),
    E400_3_NO_EXIST_USER("400-3", "해당 유저가 존재하지 않습니다."),
    E400_4_NOT_CORRECT_PASSWORD("400-4", "비밀번호가 일치하지 않습니다."),
    E400_5_NOT_EXIST_REFRESHTOKEN("400-5",  "존재하지 않는 리프레시 토큰입니다."),
    E400_6_CANCEL_REPORT_FAILED("400-6", "신고 취소 실패"),
    E400_7_ALREADY_REPORT("400-7", "이미 신고하셨습니다."),
    E404_1_DATA_NOT_FIND("404-1", "해당 데이터를 찾을 수 없습니다."),
    E404_2_YOU_ARE_NOT_ADMIN("404-2", "관리자만 처리할 수 있는 기능입니다."),
    E400_1_CREATE_FAILED("400-1","등록 실패"),
    E400_8_ALREADY_PASSED("400-8", "이미 통과된 노트입니다.수정이 불가능합니다."),
    E400_9_COUNT_ALREADY_FULL("400-9", "이미 등록 가능한 횟수가 가득 찼습니다."),
    E400_10_VIDEO_LESS_THAN_5_CANNOT_PUBLISH("400-10","영상이 일정 개수 이하이면 강좌를 공개할 수 없습니다."),
    E400_11_AlREADY_VISITED("400-11","이미 출석체크 되었습니다.");

    private  final String code;
    private final String message;

    // enum 생성자
    CodeMsg(String code, String message){
        this.code = code;
        this.message = message;
    }

    // 코드를 반환하는 메소드
    public String getCode() {
        return code;
    }

    // 메시지를 반환하는 메소드
    public String getMessage() {
        return message;
    }
}
