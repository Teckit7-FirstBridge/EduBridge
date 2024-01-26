package com.ll.edubridge.domain.course.video.dto;

import com.ll.edubridge.domain.course.video.entity.Video;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class VideoDto {
    @NonNull
    private Long id;

    @Size(max = 150)
    @NotBlank
    private String url;

    @Size(max = 300)
    private String overView;

    @NotEmpty
    private String imgUrl;

    @NonNull
    private Long courseId;


    public VideoDto(Video video) {
        this.id = video.getId();
        this.url = video.getUrl();
        this.overView = video.getOverView();
        this.courseId = video.getCourse().getId();
        this.imgUrl = video.getImgUrl();
    }

    public VideoDto() {

    }
}
