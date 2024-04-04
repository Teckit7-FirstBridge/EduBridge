package com.ll.edubridge.domain.member.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class NickNameDto {

    private String nickName;

    public NickNameDto(String nickName) {
        this.nickName = nickName;
    }
}
