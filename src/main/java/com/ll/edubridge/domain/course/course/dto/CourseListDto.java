package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.CourseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;

@Getter
public class CourseListDto {

    private Long id;
    private String title;
    private String imgUrl;
    private String overView;
    private int voteCount;
    private boolean likedByCurrentUser;
    private Member writer;
    private String hashtags;


    public CourseListDto(Course course, Member member) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.imgUrl = course.getImgUrl();
        this.overView = course.getOverView();
        this.voteCount = course.getCourseVoters().size();
        this.likedByCurrentUser = course.getCourseVoters().contains(new CourseVoter(member,course));
        this.writer = course.getWriter();
        this.hashtags = course.getHashtags();
    }

    public CourseListDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
    }

    public CourseListDto() {

    }

}
