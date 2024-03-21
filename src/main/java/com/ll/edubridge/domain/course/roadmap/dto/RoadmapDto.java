package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
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
    private List<CourseRoadmap> curriculum;

    @NonNull
    private String hashtags;

    @NonNull
    private Member owner;

    @NonNull
    private List<Roadmap> roadmapList;

    public RoadmapDto(List<Roadmap> roadmapList) {
        this.roadmapList = roadmapList;
    }

    public RoadmapDto(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
        this.overView = roadmap.getOverView();
        this.curriculum = roadmap.getCurriculum();
        this.hashtags = roadmap.getHashtags();
        this.owner = roadmap.getOwner();
    }


    public RoadmapDto() {}
}