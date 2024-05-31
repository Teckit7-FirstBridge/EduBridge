package com.ll.edubridge.domain.member.member.dto;

import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MemberDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String name;
    @NonNull
    private String username;
    @NonNull
    private String profileImgUrl;
    @NonNull
    private List<String> authorities;
    @NonNull
    private boolean visitedToday;
    @NonNull
    private int dailyGoal;
    @NonNull
    private int dailyAchievement;
  
    private int point;

    private int registerCount;

    private String uuid;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.name = member.getNickname();
        this.username = member.getUsername();
        this.profileImgUrl = member.getProfileImgUrlOrDefault();
        this.authorities = member.getAuthoritiesAsStringList();
        this.visitedToday = member.isVisitedToday();
        this.dailyGoal = member.getDailyGoal();
        this.dailyAchievement = member.getDailyAchievement();
        this.point = member.getPoint();
        this.registerCount = member.getRegisterCount();
        this.uuid = member.getUuid();
    }
}