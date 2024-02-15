package com.ll.edubridge.domain.course.video.controller;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.mapper.VideoMapper;
import com.ll.edubridge.domain.course.video.service.VideoService;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/courses", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "VideoController", description = "강의 CRUD 컨트롤러")
public class ApiV1VideoController {
    private final VideoService videoService;
    private final CourseService courseService;
    private final Rq rq;

    @GetMapping("/{courseId}/videos")
    @Operation(summary = "강의 리스트")
    public RsData<List<VideoDto>> getVideos(@PathVariable("courseId") Long courseId) {

        Course course = courseService.getCourse(courseId);
        if(course == null) {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }

        List<Video> videos = videoService.findByCourseId(courseId);
        List<VideoDto> videoDtoList = videos.stream()
                .map(video -> VideoMapper.toDto(video,rq.getMember().getId()))
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                videoDtoList
        );
    }

    @GetMapping("/{courseId}/videos/{id}")
    @Operation(summary = "특정 강의")
    public RsData<VideoDto> getVideos(@PathVariable("courseId") Long courseId,
                                      @PathVariable("id") Long id) {

        Course course = courseService.getCourse(courseId);
        if(course == null) {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }

        Video video = videoService.findByCourseIdAndId(courseId, id);
        if (video == null) {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }

        VideoDto videoDto = new VideoDto(video);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                videoDto
        );
    }

}