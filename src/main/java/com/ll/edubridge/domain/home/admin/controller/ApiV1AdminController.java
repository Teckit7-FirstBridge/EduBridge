package com.ll.edubridge.domain.home.admin.controller;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.home.admin.dto.RecentCourseDto;
import com.ll.edubridge.domain.home.admin.dto.RecentMemberDto;
import com.ll.edubridge.domain.home.admin.dto.ReportedPostDto;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.service.PostService;
import com.ll.edubridge.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RestController
@RequestMapping(value = "/api/v1/admin", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1AdminController", description = "관리자 컨트롤러")
public class ApiV1AdminController {
    private final CourseService courseService;
    private final MemberService memberService;
    private final PostService postService;

    @GetMapping(value = "/courses")
    @Operation(summary = "강좌 최신순")
    public RsData<List<RecentCourseDto>> getRecentCourses() {

        List<Course> recentCourses = courseService.findLatestCourse(5);

        List<RecentCourseDto> courseList = recentCourses.stream()
                .map(RecentCourseDto::new)
                .toList();

        return RsData.of(
                "200-1",
                "성공",
                courseList
        );
    }

    @GetMapping(value = "/reports")
    @Operation(summary = "신고 게시물 최신순")
    public RsData<List<ReportedPostDto>> getReportedPosts() {

        List<Post> reportedPosts = postService.reportedPosts(5);

        List<ReportedPostDto> reportedPostList = reportedPosts.stream()
                .map(ReportedPostDto::new)
                .toList();

        return RsData.of(
                "200-1",
                "성공",
                reportedPostList
        );
    }

    @GetMapping(value = "/members")
    @Operation(summary = "회원 최신순")
    public RsData<List<RecentMemberDto>> getMembers() {

        List<Member> recentMembers = memberService.recentMembers(5);

        List<RecentMemberDto> recentMemberList = recentMembers.stream()
                .map(RecentMemberDto::new)
                .toList();

        return RsData.of(
                "200-1",
                "성공",
                recentMemberList
        );
    }
}
