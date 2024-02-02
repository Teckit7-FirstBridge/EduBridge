package com.ll.edubridge.domain.course.course.controller;


import com.ll.edubridge.domain.course.course.dto.CourseAuthDto;
import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.courseEnroll.service.CourseEnrollService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.KwTypeCourse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/courses", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CourseController", description = "강좌 CRUD 컨트롤러")
public class ApiV1CourseController {
    private final CourseService courseService;
    private final Rq rq;
    private final CourseEnrollService courseEnrollService;

    @Getter
    public static class GetCoursesResponsebody {
        @NonNull
        private final List<CourseDto> items;

        public GetCoursesResponsebody(Page<Course> page) {
            this.items = page.getContent().stream()
                    .map(course -> new CourseDto(course))
                    .toList();
        }
    }

    @GetMapping(value = "")
    @Operation(summary = "강좌 다건 조회")
    public RsData<GetCoursesResponsebody> getCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeCourse kwType
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Course> coursePage = courseService.findByKw(kwType, kw, null, pageable);

        GetCoursesResponsebody responseBody = new GetCoursesResponsebody(coursePage);

        return RsData.of(
                "200-1",
                Msg.CHECK.getMsg(),
                responseBody
        );
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "강좌 상세 조회")
    public RsData<CourseDto> getCourse(@PathVariable("courseId") Long courseId) {
        Course course = courseService.getCourse(courseId);
        CourseDto courseDto = new CourseDto(course);

        return RsData.of("200-1", Msg.CHECK.getMsg(), courseDto);
    }

    @GetMapping("/{courseId}/auth")
    @Operation(summary = "해당 멤버가 해당 강좌를 수강 중인지")
    public RsData<CourseAuthDto> getCourseAuth(@PathVariable("courseId") Long courseId){

        CourseAuthDto courseAuthDto=new CourseAuthDto(courseEnrollService.isEnroll(courseId));

        return RsData.of("200-1",Msg.CHECK.getMsg(),courseAuthDto);

    }

}