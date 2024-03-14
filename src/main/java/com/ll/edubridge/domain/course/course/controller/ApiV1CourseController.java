package com.ll.edubridge.domain.course.course.controller;


import com.ll.edubridge.domain.course.course.dto.CourseAuthDto;
import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.courseEnroll.service.CourseEnrollService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
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
    public class GetCoursesResponsebody {
        @NonNull
        private final List<CourseDto> items;

        public GetCoursesResponsebody(Page<Course> page) {
            this.items = page.getContent().stream()
                    .map(course -> new CourseDto(course,rq.getMember()))
                    .toList();
        }
    }

    @GetMapping(value = "")
    @Operation(summary = "강좌 다건 조회")
    public RsData<GetCoursesResponsebody> getCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeCourse kwType,
            @RequestParam(defaultValue = "") String grade
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<Course> coursePage;
        if (rq.isAdmin()) {
            coursePage = courseService.findByKwAdmin(kwType, kw, null, grade, pageable);
        } else {
            coursePage = courseService.findByKw(kwType, kw, null, grade, pageable);
        }

        GetCoursesResponsebody responseBody = new GetCoursesResponsebody(coursePage);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                responseBody

        );
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "강좌 상세 조회")
    public RsData<CourseDto> getCourse(@PathVariable("courseId") Long courseId) {
        Course course = courseService.getCourse(courseId);
        CourseDto courseDto = new CourseDto(course,rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(), courseDto);
    }

    @GetMapping("/{courseId}/auth")
    @Operation(summary = "해당 멤버가 해당 강좌를 수강 중인지")
    public RsData<CourseAuthDto> getCourseAuth(@PathVariable("courseId") Long courseId){

        CourseAuthDto courseAuthDto=new CourseAuthDto(courseEnrollService.isEnroll(courseId));

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(), courseAuthDto);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "강좌 좋아요")
    public RsData<Void> vote(@PathVariable("id") Long id) {
        Member member = rq.getMember();

        if (!courseService.canLike(member, courseService.getCourse(id))) {
            throw new GlobalException(CodeMsg.E400_1_ALREADY_RECOMMENDED.getCode(),CodeMsg.E400_1_ALREADY_RECOMMENDED.getMessage());
        }

        courseService.vote(id, member);

        return RsData.of(Msg.E200_4_RECOMMEND_SUCCEED.getCode(),
                Msg.E200_4_RECOMMEND_SUCCEED.getMsg());
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "강좌 좋아요 취소")
    public RsData<Void> deleteVote(@PathVariable("id") Long id) {
        Member member = rq.getMember();

        if (!courseService.canCancelLike(member, courseService.getCourse(id))) {
            throw new GlobalException(CodeMsg.E400_2_NOT_RECOMMENDED_YET.getCode(),CodeMsg.E400_2_NOT_RECOMMENDED_YET.getMessage());
        }

        courseService.deleteVote(id, member);


        return RsData.of(Msg.E200_5_CANCEL_RECOMMEND_SUCCEED.getCode(),
                Msg.E200_5_CANCEL_RECOMMEND_SUCCEED.getMsg());
    }


}