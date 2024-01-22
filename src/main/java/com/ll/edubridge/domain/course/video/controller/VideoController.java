package com.ll.edubridge.domain.course.video.controller;

import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.service.VideoService;
import com.ll.edubridge.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/videos", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "VideoController", description = "강의 CRUD 컨트롤러")
public class VideoController {
    private final VideoService videoService;

    @Getter
    public static class GetVideosResponseBody {

        @NonNull
        private final List<VideoDto> videos;

        public GetVideosResponseBody(Page<Video> items) {
            this.videos = items.stream()
                    .map(VideoDto::new)
                    .toList();
        }
    }

    @Operation(summary = "강의 리스트")
    @GetMapping("/api/v1/courses/{id}/videos")
    public RsData<com.ll.edubridge.domain.course.video.controller.VideoController.GetVideosResponseBody> getVideos(@PathVariable("id") Integer id,
                                                                                                                   @RequestParam(value = "page", defaultValue = "0") int page) {
        int courseId = id;
        Page<Video> videos = videoService.findAll(page, courseId); // findByCourseId

        return RsData.of(
                "200-1",
                "성공",
                new com.ll.edubridge.domain.course.video.controller.VideoController.GetVideosResponseBody(videos)
        );
    }
}