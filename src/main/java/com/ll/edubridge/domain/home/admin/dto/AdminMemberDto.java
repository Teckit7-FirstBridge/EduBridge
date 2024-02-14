package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
@Getter
public class AdminMemberDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String name;
    @NonNull
    private boolean report;

    public AdminMemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.name = member.getName();
        this.report=member.isReport();
    }
}
