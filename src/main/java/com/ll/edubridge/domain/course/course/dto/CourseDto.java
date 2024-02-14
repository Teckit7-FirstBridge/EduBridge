package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;

@Getter
public class CourseDto {

    private Long id;
    private String title;
    private String notice;
    private String imgUrl;
    private String overView;
    private String grade;
    private int price;
    private int voteCount;
    private boolean likedByCurrentUser;
    private int videoCount;
    private Boolean confirm;
    private int enrollCount;

    public CourseDto(Course course, Member member) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.notice = course.getNotice();
        this.imgUrl = course.getImgUrl();
        this.overView = course.getOverView();
        this.grade = course.getGrade();
        this.price = course.getPrice();
        this.voteCount = course.getVoter().size();
        this.likedByCurrentUser = course.getVoter().contains(member);
        this.videoCount = course.getVideoList().size();
        this.confirm = course.getConfirm();
        this.enrollCount = course.getCourseEnrollList().size();
    }

    public CourseDto() {

    }

}