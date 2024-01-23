package com.ll.edubridge.domain.course.video.controller;

import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.service.VideoService;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "VideoController", description = "강의 CRUD 컨트롤러")
public class VideoController {
    private final VideoService videoService;

    @Getter
    public static class GetVideosResponseBody {

        @NonNull
        private final List<VideoDto> videos;

        public GetVideosResponseBody(List<Video> items) {
            this.videos = items.stream()
                    .map(VideoDto::new)
                    .toList();
        }
    }

    @GetMapping("/courses/{courseId}/videos")
    @Operation(summary = "강의 리스트")
    public RsData<GetVideosResponseBody> getVideos(@PathVariable("courseId") Long courseId) {
        List<Video> videos = videoService.findAll(courseId); // findByCourseId
        GetVideosResponseBody responseBody = new GetVideosResponseBody(videos);

        return RsData.of(
                "200-1",
                "성공",
                responseBody
        );
    }

    @GetMapping("/courses/{courseId}/{id}")
    @Operation(summary = "특정 강의")
    public RsData<GetVideosResponseBody> getVideos(@PathVariable("courseId") Long courseId,
                                                   @PathVariable("id") Long id) {

        Video video = videoService.findById(courseId, id);
        if (video == null) {
            throw new GlobalException("404-1", "해당 강의를 찾을 수 없습니다.");
        }

        GetVideosResponseBody responseBody = new GetVideosResponseBody(Collections.singletonList(video));

        return RsData.of(
                "200-1",
                "성공",
                responseBody
        );
    }

    @PostMapping("/admin/{courseId}/videos")
    @Operation(summary = "강의 등록")
    public RsData<VideoDto> createVideo(@PathVariable("courseId") Long courseId,
                                        @RequestBody VideoDto videoDto) {
        Video video = videoService.create(videoDto);
        VideoDto createdVideoDto = new VideoDto(video);

        return RsData.of("200-0", "등록 성공", createdVideoDto);
    }

    @PutMapping("/admin/{courseId}/videos")
    @Operation(summary = "강의 수정")
    public RsData<VideoDto> modify(
            @PathVariable("courseId") Long courseId,
            @PathVariable Long id,
            @RequestBody VideoDto videoDto) {

        if (!videoService.haveAuthority())
            throw new GlobalException("403-1", "권한이 없습니다.");

        Video modifyVideo = videoService.modify(id, videoDto);

        VideoDto modifyVideoDto = new VideoDto(modifyVideo);

        return RsData.of("200-2", "수정 성공", modifyVideoDto);
    }

    @DeleteMapping("/admin/{courseId}/videos")
    @Operation(summary = "강의 삭제")
    public RsData<Empty> delete(@PathVariable("courseId") Long courseId, @PathVariable Long id) {

        if (!videoService.haveAuthority())
            throw new GlobalException("403-1", "권한이 없습니다.");

        videoService.delete(id);

        return RsData.of("200-3", "삭제 성공");
    }
}