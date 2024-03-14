package com.ll.edubridge.domain.member.member.dto;

import lombok.Getter;


@Getter
public class NickNameDto {

    private String nickName;
    public NickNameDto(String nickName) {
        this.nickName = nickName;
    }
    public NickNameDto() {
    }
}
