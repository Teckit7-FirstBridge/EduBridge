package com.ll.edubridge.domain.course.course.controller;


import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RestController
@RequestMapping(value = "/api/v1/courses", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CourseController", description = "강좌 CRUD 컨트롤러")
@SecurityRequirement(name = "bearerAuth") // 옵션

public class ApiV1CourseController {

    @AllArgsConstructor
    @Getter
    public static class GetCoursesResponsebody{
        List<CourseDto> items;
    }
    @Operation(summary = "전체 강좌 목록 조회")
    @GetMapping("/")
    public RsData<GetCoursesResponsebody> getCourses(){
        List<Course> courses = CourseService.findAll();
        return RsData.of(
                "200-1",
                "성공",
                new GetCoursesResponsebody(
                courses.
                        stream().
                        map(CourseDto::new).
                        toList()
        ));
    }

    private final CourseService courseService;
    @Operation(summary = "강좌 상세 조회")
    @GetMapping("/{course-id}")
    public RsData<CourseDto> getCourse(@PathVariable("course-id") Long id){
        Course course = courseService.findById(id);
        return RsData.of("200-1",
                "성공",
                new CourseDto(course));
    }
}