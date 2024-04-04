package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class
RoadmapListDto {
    private String title;
    private Long id;

    public RoadmapListDto(Roadmap roadmap) {
        this.title = roadmap.getTitle();
        this.id = roadmap.getId();
    }
}
