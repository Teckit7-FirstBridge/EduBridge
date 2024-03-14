package com.ll.edubridge.domain.course.video.mapper;

import com.ll.edubridge.domain.course.summaryNote.dto.SummaryNoteDto;
import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;

import java.util.List;
import java.util.stream.Collectors;

public class VideoMapper {

    public static VideoDto toDto(Video video, Long memberId) {
        List<SummaryNoteDto> filteredSummaryNotes = video.getSummaryNotes().stream()
            .filter(summaryNote -> summaryNote.getWriter().getId().equals(memberId))
            .map(SummaryNoteDto::new) // 가정: SummaryNote를 SummaryNoteDto로 변환하는 매퍼
            .collect(Collectors.toList());

        return VideoDto.builder()
                .id(video.getId())
                .courseId(video.getCourse().getId())
            .url(video.getUrl())
            .imgUrl(video.getImgUrl())
            .overView(video.getOverView())
            .summaryNotes(filteredSummaryNotes)
                .keywords(video.getKeywords())
                .title(video.getTitle())
            .build();
    }
}