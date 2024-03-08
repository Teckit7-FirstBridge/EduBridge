package com.ll.edubridge.domain.course.courseEnroll.controller;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.courseEnroll.dto.CourseEnrollDto;
import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.domain.course.courseEnroll.service.CourseEnrollService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.point.point.entity.PointType;
import com.ll.edubridge.domain.point.point.service.PointService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/enroll", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CourseEnrollController", description = "수업 테이블 컨트롤러")
public class ApiV1CourseEnrollController {
    private final CourseEnrollService courseEnrollService;
    private final CourseService courseService;
    private final Rq rq;
    private final PointService pointService;

    @Getter
    public static class GetCourseEnrollResponsebody{
        @NonNull
        private final List<CourseEnrollDto> items;

        public GetCourseEnrollResponsebody(Page<CourseEnroll> page) {
            this.items = page.getContent().stream()
                    .map(CourseEnrollDto::new)
                    .toList();
        }
    }

    @PostMapping("/{courseId}")
    @Operation(summary = "수강 등록")
    public RsData<Void> create(@PathVariable("courseId") Long courseId) {

        Member member = rq.getMember(); //  현재 로그인한 사용자의 정보
        int point = member.getPoint();
        Course course = courseService.getCourse(courseId);
        int price = course.getPrice();
        if (point >= price) {
            CourseEnroll courseEnroll = courseEnrollService.create(rq.getMember(), course, point, price);
            pointService.subPoint(PointType.Enroll, member, price); // 포인트 내역 추가
            return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
        } else {
            throw new GlobalException(CodeMsg.E400_1_CREATE_FAILED.getCode(), CodeMsg.E400_1_CREATE_FAILED.getMessage());
        }
    }

    @GetMapping("")
    @Operation(summary = "수업 목록 조회")
    public RsData<GetCourseEnrollResponsebody> getEnrollList(@RequestParam(value = "page", defaultValue = "1") int page){
        int pageSize = AppConfig.getBasePageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        Page<CourseEnroll> courseEnrolls = courseEnrollService.findByMemberId(pageable);
        GetCourseEnrollResponsebody responseBody = new GetCourseEnrollResponsebody(courseEnrolls);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                responseBody
        );
    }


}
