package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.domain.member.member.entity.Member;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
public class RoadmapDto {
    @NonNull
    private Long id;

    @Size(max = 50)
    private String title;

    @Size(max = 300)
    private String overView;

    @NonNull
    private List<Course> curriculum;

    @NonNull
    private String hashtags;

    @NonNull
    private String owner;

    public RoadmapDto(Roadmap roadmap, Member member) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
        this.overView = roadmap.getOverView();
        this.curriculum = roadmap.getCurriculum();
        this.hashtags = roadmap.getHashtags();
        this.owner = member.getUsername();
    }

    public RoadmapDto(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
        this.overView = roadmap.getOverView();
        this.curriculum = roadmap.getCurriculum();
        this.hashtags = roadmap.getHashtags();
    }

    public RoadmapDto() {}
}
