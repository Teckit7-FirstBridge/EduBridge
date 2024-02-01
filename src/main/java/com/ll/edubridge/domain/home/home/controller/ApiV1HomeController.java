package com.ll.edubridge.domain.home.home.controller;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.controller.ApiV1PostController;
import com.ll.edubridge.domain.post.post.dto.PostDto;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.service.PostService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "ApiV1HomeController", description = "홈 컨트롤러")
public class ApiV1HomeController {
    private final Rq rq;

    private final CourseService courseService;
    @Getter
    public static class GetPostsResponseBody {
        @NonNull
        private final List<CourseDto> items;

        public GetPostsResponseBody(List<Course> courses, Member currentUser) {
            this.items = courses.stream()
                    .map(course -> new CourseDto(course))
                    .toList();
        }
    }

    @GetMapping("")
    @Operation(summary = "홈화면 최신 강좌 N개조회")
    public RsData<GetPostsResponseBody> getPosts() {

        List<Course> courses = courseService.findLatestCourse(6);
        GetPostsResponseBody responseBody = new GetPostsResponseBody(courses, rq.getMember());

        return RsData.of(
                "200-1",
                Msg.CHECK.getMsg(),
                responseBody
        );
    }
}
