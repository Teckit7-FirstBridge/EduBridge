package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.List;

@Getter
public class CreateRoadmapDto {
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

    public CreateRoadmapDto(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
        this.overView = roadmap.getOverView();
        this.curriculum = roadmap.getCurriculum();
        this.hashtags = roadmap.getHashtags();
    }
}
