package com.ll.edubridge.domain.home.home.controller;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
@Tag(name = "ApiV1HomeController", description = "홈 컨트롤러")
public class ApiV1HomeController {
    private final Rq rq;

    private final CourseService courseService;
    @Getter
    public class GetPostsResponseBody {
        @NonNull
        private final List<CourseDto> items;

        public GetPostsResponseBody(List<Course> courses, Member currentUser) {
            this.items = courses.stream()
                    .map(course -> new CourseDto(course,rq.getMember()))
                    .toList();
        }
    }

    @GetMapping("")
    @Operation(summary = "홈화면 좋아요 순으로 강좌 N개조회")
    public RsData<GetPostsResponseBody> getPosts() {

        List<Course> courses = courseService.findTopVotedCourses(6);
        GetPostsResponseBody responseBody = new GetPostsResponseBody(courses, rq.getMember());
        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                responseBody
        );
    }
}
