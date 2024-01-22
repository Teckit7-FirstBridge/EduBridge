package com.ll.edubridge.domain.course.video.controller;

import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.service.VideoService;
import com.ll.edubridge.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/videos", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1PostController", description = "강의 CRUD 컨트롤러")
public class VideoController {
    private final VideoService videoService;

    @Getter
    public static class GetVideosResponseBody {

//        @NonNull
//        private final List<VideoDto> videos;
//
//        public GetVidoesResponseBody(List<Post> items) {
//            this.videos = items.stream()
//                    .map(VideoDto::new)
//                    .toList();
//        }

        public GetVideosResponseBody() {
        }
    }

    @Operation(summary = "강의 리스트")
    @GetMapping("")
    public RsData<com.ll.edubridge.domain.course.video.controller.VideoController.GetVideosResponseBody> getVideos(@RequestParam(value="page", defaultValue="0") int page) {
        int courseId =  0; // TODO : course.getId();
        Page<Video> videos = videoService.findAll(page, courseId); // findByCourseId

        return RsData.of(
                "200-1",
                "성공",
                new com.ll.edubridge.domain.course.video.controller.VideoController.GetVideosResponseBody()
        );
    }
}