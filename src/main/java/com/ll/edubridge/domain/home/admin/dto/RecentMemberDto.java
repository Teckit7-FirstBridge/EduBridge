package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
@Getter
public class RecentMemberDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String name;

    public RecentMemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.name = member.getName();
    }
}
