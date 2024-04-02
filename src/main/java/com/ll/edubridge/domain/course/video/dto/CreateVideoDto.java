package com.ll.edubridge.domain.course.video.dto;

import com.ll.edubridge.domain.course.video.entity.Video;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateVideoDto {
    @NotEmpty
    @Size(max = 150)
    private String url;

    @NotEmpty
    @Size(max = 300)
    private String overView;

    private Long courseId;

    @NotEmpty
    @Size(max = 100)
    private String keywords;

    @NotEmpty
    private String title;

    public CreateVideoDto(Video video) {
        this.url = video.getUrl();
        this.overView = video.getOverView();
        this.courseId = video.getCourse().getId();
        this.keywords  = video.getKeywords();
        this.title = video.getTitle();
    }

    public CreateVideoDto() {

    }
}
