package com.ll.edubridge.domain.course.video.controller;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.video.dto.CreateVideoDto;
import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.mapper.VideoMapper;
import com.ll.edubridge.domain.course.video.service.VideoService;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "VideoController", description = "강의 CRUD 컨트롤러")
public class ApiV1VideoController {
    private final VideoService videoService;
    private final CourseService courseService;
    private final Rq rq;

    @GetMapping("/courses/{courseId}/videos")
    @Operation(summary = "강의 리스트")
    public RsData<List<VideoDto>> getVideos(@PathVariable("courseId") Long courseId) {

        Course course = courseService.getCourse(courseId);
        if(course == null) {
            throw new GlobalException(CodeMsg.E404_1_COURSE_NOT_FIND.getCode(), CodeMsg.E404_1_COURSE_NOT_FIND.getMessage());
        }

        List<Video> videos = videoService.findByCourseId(courseId);
        List<VideoDto> videoDtoList = videos.stream()
                .map(video -> VideoMapper.toDto(video,rq.getMember().getId()))
                .toList();

        return RsData.of(
                "200-1",
                Msg.CHECK.getMsg(),
                videoDtoList
        );
    }

    @GetMapping("/courses/{courseId}/videos/{id}")
    @Operation(summary = "특정 강의")
    public RsData<VideoDto> getVideos(@PathVariable("courseId") Long courseId,
                                      @PathVariable("id") Long id) {

        Course course = courseService.getCourse(courseId);
        if(course == null) {
            throw new GlobalException(CodeMsg.E404_1_COURSE_NOT_FIND.getCode(), CodeMsg.E404_1_COURSE_NOT_FIND.getMessage());
        }

        Video video = videoService.findByCourseIdAndId(courseId, id);
        if (video == null) {
            throw new GlobalException(CodeMsg.E404_1_COURSE_NOT_FIND.getCode(), CodeMsg.E404_1_COURSE_NOT_FIND.getMessage());
        }

        VideoDto videoDto = new VideoDto(video);

        return RsData.of(
                "200-1",
                Msg.CHECK.getMsg(),
                videoDto
        );
    }

    @PostMapping("/admin/{courseId}/videos")
    @Operation(summary = "강의 등록")
    public RsData<CreateVideoDto> createVideo(@PathVariable("courseId") Long courseId,
                                              @RequestBody CreateVideoDto createVideoDto) {

        if (!videoService.haveAuthority(courseId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Video video = videoService.create(createVideoDto);
        CreateVideoDto createdVideoDto = new CreateVideoDto(video);

        return RsData.of("200-0", Msg.CREATE.getMsg(), createdVideoDto);
    }

    @PutMapping("/admin/{courseId}/videos/{id}")
    @Operation(summary = "강의 수정")
    public RsData<VideoDto> modify(@PathVariable("courseId") Long courseId,
                                   @PathVariable("id") Long id,
                                   @RequestBody VideoDto videoDto) {

        if (!videoService.haveAuthority(courseId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Course course = courseService.getCourse(courseId);
        if(course == null) {
            throw new GlobalException(CodeMsg.E404_1_COURSE_NOT_FIND.getCode(), CodeMsg.E404_1_COURSE_NOT_FIND.getMessage());
        }

        Video video = videoService.findByCourseIdAndId(courseId, id);
        if (video == null) {
            throw new GlobalException(CodeMsg.E404_1_COURSE_NOT_FIND.getCode(), CodeMsg.E404_1_COURSE_NOT_FIND.getMessage());
        }

        Video modifyVideo = videoService.modify(id, videoDto);

        VideoDto modifyVideoDto = new VideoDto(modifyVideo);

        return RsData.of("200-2", Msg.MODIFY.getMsg(), modifyVideoDto);
    }

    @DeleteMapping("/admin/{courseId}/videos/{id}")
    @Operation(summary = "강의 삭제")
    public RsData<Empty> delete(@PathVariable("courseId") Long courseId,
                                @PathVariable("id") Long id) {

        if (!videoService.haveAuthority(courseId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(),CodeMsg.E403_1_NO.getMessage());

        Course course = courseService.getCourse(courseId);
        if(course == null) {
            throw new GlobalException(CodeMsg.E404_1_COURSE_NOT_FIND.getCode(), CodeMsg.E404_1_COURSE_NOT_FIND.getMessage());
        }

        Video video = videoService.findByCourseIdAndId(courseId, id);
        if (video == null) {
            throw new GlobalException(CodeMsg.E404_1_COURSE_NOT_FIND.getCode(), CodeMsg.E404_1_COURSE_NOT_FIND.getMessage());
        }

        videoService.delete(id);

        return RsData.of("200-3", Msg.DELETE.getMsg());
    }
}