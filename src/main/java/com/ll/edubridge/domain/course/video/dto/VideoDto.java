package com.ll.edubridge.domain.course.video.dto;

import com.ll.edubridge.domain.course.summaryNote.dto.SummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.video.entity.Video;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
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

    @NonNull
    private List<SummaryNoteDto> summaryNotes;

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
