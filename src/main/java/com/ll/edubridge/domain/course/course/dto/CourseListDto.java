package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.CourseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class CourseListDto {

    @NonNull
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String imgUrl;
    @NonNull
    private String overView;
    @NonNull
    private int voteCount;
    @NonNull
    private boolean likedByCurrentUser;
    @NonNull
    private Member writer;
    @NonNull
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
