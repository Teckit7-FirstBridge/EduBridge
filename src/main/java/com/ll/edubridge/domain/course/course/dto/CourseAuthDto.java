package com.ll.edubridge.domain.course.course.dto;


import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;

@Getter
public class CourseAuthDto {

    private boolean auth;

    private int point;

    public CourseAuthDto(Boolean auth, Member member){
        this.auth=auth;
        this.point=member.getPoint();
    }

    public CourseAuthDto(){

    }

}
