package com.ll.edubridge.global.exceptions;

public enum CodeMsg {
    E403_1_NO("403-1", "권한이 없습니다."),
    E400_1_ALREADY_RECOMMENDED("400-1", "이미 추천하셨습니다"),
    E400_2_NOT_RECOMMENDED_YET("400-2", "추천을 하지 않았습니다."),
    E400_3_NO_EXIST_USER("400-3", "해당 유저가 존재하지 않습니다."),
    E400_4_NOT_CORRECT_PASSWORD("400-4", "비밀번호가 일치하지 않습니다."),
    E400_5_NOT_REFRESHTOKEN("400-5",  "존재하지 않는 리프레시 토큰입니다."),
    E404_1_DATA_NOT_FIND("404-1", "해당 데이터를 찾을 수 없습니다.");

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
