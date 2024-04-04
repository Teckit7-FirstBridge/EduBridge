package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.courseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
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
    private String writer;
    @NonNull
    private String hashtags;
    @NonNull
    private boolean confirm;


    public CourseListDto(Course course, Member member) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.imgUrl = course.getImgUrl();
        this.overView = course.getOverView();
        this.voteCount = course.getCourseVoters().size();
        this.likedByCurrentUser = course.getCourseVoters().contains(new CourseVoter(member,course));
        this.writer = course.getWriter().getNickname();
        this.hashtags = course.getHashtags();
        this.confirm = course.getConfirm();
    }

}
