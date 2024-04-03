package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.dto.CourseListDto;
import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.domain.member.member.entity.Member;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;

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
    private String hashtags;

    @NonNull
    private Long owner_id;

    @NonNull
    private String owner_nickname;

    @NonNull
    private List<CurriculumDto> curriculum;


    public RoadmapDto(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
        this.overView = roadmap.getOverView();
        this.hashtags = roadmap.getHashtags();
        this.owner_nickname = roadmap.getOwner().getNickname();
        this.owner_id = roadmap.getOwner().getId();
        this.curriculum = roadmap.getCurriculum().stream().map(courseRoadmap -> {return new CurriculumDto(courseRoadmap.getCourse(),courseRoadmap.getCourseOrder());}).collect(Collectors.toList());
    }


    public RoadmapDto() {}
}