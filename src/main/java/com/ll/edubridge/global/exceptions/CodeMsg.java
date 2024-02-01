package com.ll.edubridge.global.exceptions;

public enum CodeMsg {
    E403_1_NO("403-1", "권한이 없습니다."),
    E400_1_ALREADY_RECOMMENDED("400-1", "이미 추천하셨습니다"),
    E400_2_NOT_RECOMMENDED_YET("400-2", "추천을 하지 않았습니다."),
    E404_1_COURSE_NOT_FIND("404-1", "해당 강좌를 찾을 수 없습니다.");

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
