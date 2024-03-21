package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.CourseVoter.entity.CourseVoter;
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
    private int price;
    private int voteCount;
    private boolean likedByCurrentUser;
    private int videoCount;
    private Boolean confirm;
    private int enrollCount;
    private Member writer;
    private String hashtags;


    public CourseDto(Course course, Member member) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.notice = course.getNotice();
        this.imgUrl = course.getImgUrl();
        this.overView = course.getOverView();
        this.price = course.getPrice();
        this.voteCount = course.getCourseVoters().size();
        this.likedByCurrentUser = course.getCourseVoters().contains(new CourseVoter(member,course));
        this.videoCount = course.getVideoList().size();
        this.confirm = course.getConfirm();
        this.enrollCount = course.getCourseEnrollList().size();
        this.writer = course.getWriter();
        this.hashtags = course.getHashtags();
    }

    public CourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
    }

    public CourseDto() {

    }

}