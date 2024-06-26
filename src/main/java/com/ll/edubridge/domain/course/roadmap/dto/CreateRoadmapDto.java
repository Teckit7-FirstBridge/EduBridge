package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CreateRoadmapDto {

    @Size(max = 50)
    private String title;

    @Size(max = 300)
    private String overView;

    @NonNull
    private String hashtags;

    public CreateRoadmapDto(Roadmap roadmap) {
        this.title = roadmap.getTitle();
        this.overView = roadmap.getOverView();
        this.hashtags = roadmap.getHashtags();
    }
}
