package com.ll.edubridge.domain.course.video.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.video.entity.Video;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateVideoDto {
    @Size(max = 150)
    @NotBlank
    private String url;

    @Size(max = 300)
    private String overView;

    @NotEmpty
    private Course course;

    public CreateVideoDto(Video video) {
        this.url = video.getUrl();
        this.overView = video.getOverView();
        this.course = video.getCourse();
    }
}
