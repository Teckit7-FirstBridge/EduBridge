package com.ll.edubridge.domain.home.admin.controller;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.service.SummaryNoteService;
import com.ll.edubridge.domain.home.admin.dto.RecentCourseDto;
import com.ll.edubridge.domain.home.admin.dto.RecentMemberDto;
import com.ll.edubridge.domain.home.admin.dto.RecentSummaryNoteDto;
import com.ll.edubridge.domain.home.admin.dto.ReportedPostDto;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.service.PostService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    private final SummaryNoteService summaryNoteService;

    @GetMapping(value = "/courses")
    @Operation(summary = "강좌 최신순")
    public RsData<List<RecentCourseDto>> getRecentCourses() {

        List<Course> recentCourses = courseService.findLatestCourse(5);

        List<RecentCourseDto> courseList = recentCourses.stream()
                .map(RecentCourseDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCUSSED.getCode(),
                Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(),
                courseList
        );
    }

    @GetMapping(value = "/reports")
    @Operation(summary = "신고 게시물 최신순")
    public RsData<List<ReportedPostDto>> getReportedPosts() {

        List<Post> reportedPosts = postService.reportedPosts();

        List<ReportedPostDto> reportedPostList = reportedPosts.stream()
                .map(ReportedPostDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCUSSED.getCode(),
                Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(),
                reportedPostList
        );
    }

    @GetMapping(value = "/members")
    @Operation(summary = "회원 최신순")
    public RsData<List<RecentMemberDto>> getMembers() {

        List<Member> recentMembers = memberService.recentMembers();

        List<RecentMemberDto> recentMemberList = recentMembers.stream()
                .map(RecentMemberDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCUSSED.getCode(),
                Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(),
                recentMemberList
        );
    }

    @GetMapping(value = "/summaryNotes")
    @Operation(summary = "최신 요약노트")
    public RsData<List<RecentSummaryNoteDto>> getSummeryNotes(){

        List<SummaryNote> recentSummaryNotes = summaryNoteService.recentSummaryNotes();

        List<RecentSummaryNoteDto> recentSummaryNoteList = recentSummaryNotes.stream()
                .map(RecentSummaryNoteDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCUSSED.getCode(),
                Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(),
                recentSummaryNoteList
        );
    }

    @GetMapping(value = "/reports/list")
    @Operation(summary = "신고 게시물 목록")
    public RsData<List<ReportedPostDto>> getAllReportedPosts() {

        List<Post> reportedPosts = postService.reportedPosts();

        List<ReportedPostDto> reportedPostList = reportedPosts.stream()
                .map(ReportedPostDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCUSSED.getCode(),
                Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(),
                reportedPostList
        );
    }

    @GetMapping(value = "/members/list")
    @Operation(summary = "회원 목록")
    public RsData<PageDto<RecentMemberDto>> getAllMembers(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<Member> members = memberService.findAll(pageable);

        // Page<Member>를 PageDto<RecentMemberDto>로 변환
        PageDto<RecentMemberDto> pageDto = new PageDto<>(
                members.map(RecentMemberDto::new)
        );

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCUSSED.getCode(),
                Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(),
                pageDto
        );
    }

    public record GetNotesResponseBody(@NonNull PageDto<RecentSummaryNoteDto> itemPage) {
    }

    @GetMapping(value = "/summaryNotes/list")
    @Operation(summary = "요약노트 목록")
    public RsData<GetNotesResponseBody> getAllSummeryNotes(
            @RequestParam(defaultValue = "1") int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<SummaryNote> recentSummaryNotes = summaryNoteService.findAll(pageable);

        // Page<SummaryNote>를 PageDto<RecentSummaryNoteDto>로 변환

        Page<RecentSummaryNoteDto> notePage = recentSummaryNotes.map(this::noteToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCUSSED.getCode(),
                Msg.E200_1_INQUIRY_SUCCUSSED.getMsg(),
                new GetNotesResponseBody(
                        new PageDto<>(notePage)
                )
        );
    }

    private RecentSummaryNoteDto noteToDto(SummaryNote summaryNote) {
        RecentSummaryNoteDto dto = new RecentSummaryNoteDto(summaryNote);

        return dto;
    }


}
