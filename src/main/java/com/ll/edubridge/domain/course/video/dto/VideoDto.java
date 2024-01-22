package com.ll.edubridge.domain.course.video.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.video.entity.Video;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class VideoDto {

    @Size(max = 150)
    @NotBlank
    private String url;

    @Size(max = 300)
    private String overView;

    @NotEmpty
    private Course course;

    private List<SummaryNote> summaryNotes;

    public VideoDto(Video video) {
        this.url = video.getUrl();
        this.overView = video.getOverView();
        this.course = video.getCourse();
        this.summaryNotes = video.getSummaryNotes();
    }
}
