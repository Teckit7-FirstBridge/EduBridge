package com.ll.edubridge.domain.course.course.controller;


import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.controller.ApiV1PostController;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import com.ll.edubridge.standard.base.KwTypeCourse;
import com.ll.edubridge.standard.base.KwTypeV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RestController
@RequestMapping(value = "/api/v1/courses", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CourseController", description = "강좌 CRUD 컨트롤러")
public class ApiV1CourseController {
    private final CourseService courseService;
    private final Rq rq;

    @Getter
    public static class GetCoursesResponsebody{
        @NonNull
        private final List<CourseDto> items;

        public GetCoursesResponsebody(Page<Course> page, Member currentUser) {
            this.items = page.getContent().stream()
                    .map(course -> new CourseDto(course))
                    .toList();
        }
    }

    @GetMapping(value = "")
    @Operation(summary = "강좌 다건 조회")
    public RsData<GetCoursesResponsebody> getPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeCourse kwType
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Course> coursePage = courseService.findByKw(kwType, kw, null,  pageable);

        GetCoursesResponsebody responseBody = new GetCoursesResponsebody(coursePage, rq.getMember());

        return RsData.of(
                "200-1",
                "성공",
                responseBody
        );
    }

    @GetMapping("/{course-id}")
    @Operation(summary = "강좌 상세 조회")
    public RsData<CourseDto> getCourse(@PathVariable("course-id") Long id){
        Course course = courseService.getCourse(id);
        CourseDto courseDto = new CourseDto(course);
        return RsData.of("200-1", "성공", courseDto);
    }

    @PostMapping("")
    @Operation(summary = "강좌 등록")
    public RsData<CreateCourseDto> createCourse(@RequestBody CreateCourseDto createCourseDto) {
        Course course = courseService.create(rq.getMember(), createCourseDto);

        CreateCourseDto createdCourseDto = new CreateCourseDto(course);

        return RsData.of("200-0", "등록 성공", createdCourseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "강좌 수정")
    public RsData<CourseDto> modify(
            @PathVariable("id") Long id,
            @RequestBody CourseDto courseDto) {

        if(!courseService.haveAuthority(id))
            throw new GlobalException("403-1", "권한이 없습니다.");

        Course modifyCourse = courseService.modify(id, courseDto);

        CourseDto modifyCourseDto = new CourseDto(modifyCourse);

        return RsData.of("200-2", "수정 성공", modifyCourseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "강좌 삭제")
    public RsData<Empty> delete(@PathVariable("id") Long id) {

        if(!courseService.haveAuthority(id))
            throw new GlobalException("403-1", "권한이 없습니다.");

        courseService.delete(id);

        return RsData.of("200-3", "삭제 성공");
    }
}