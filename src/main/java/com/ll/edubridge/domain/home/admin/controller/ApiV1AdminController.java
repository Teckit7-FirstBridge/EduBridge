package com.ll.edubridge.domain.home.admin.controller;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.domain.course.courseEnroll.service.CourseEnrollService;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.service.SummaryNoteService;
import com.ll.edubridge.domain.course.video.dto.CreateVideoDto;
import com.ll.edubridge.domain.course.video.dto.VideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.course.video.service.VideoService;
import com.ll.edubridge.domain.home.admin.dto.*;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.post.post.service.PostService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import com.ll.edubridge.standard.base.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private final VideoService videoService;
    private final CourseEnrollService courseEnrollService;

    private final Rq rq;

    @GetMapping(value = "/courses")
    @Operation(summary = "강좌 최신순")
    public RsData<List<AdminCourseDto>> getRecentCourses() {

        List<Course> recentCourses = courseService.findRecentCourse();

        List<AdminCourseDto> courseList = recentCourses.stream()
                .map(AdminCourseDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
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
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                reportedPostList
        );
    }

    @GetMapping(value = "/members")
    @Operation(summary = "회원 최신순")
    public RsData<List<AdminMemberDto>> getMembers() {

        List<Member> recentMembers = memberService.recentMembers();

        List<AdminMemberDto> recentMemberList = recentMembers.stream()
                .map(AdminMemberDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                recentMemberList
        );
    }

    @GetMapping(value = "/summaryNotes")
    @Operation(summary = "최신 요약노트")
    public RsData<List<AdminSummaryNoteDto>> getSummeryNotes() {

        List<SummaryNote> recentSummaryNotes = summaryNoteService.recentSummaryNotes();

        List<AdminSummaryNoteDto> recentSummaryNoteList = recentSummaryNotes.stream()
                .map(AdminSummaryNoteDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                recentSummaryNoteList
        );
    }

    @GetMapping(value = "/qna")
    @Operation(summary = "최신 문의")
    public RsData<List<AdminQnaDto>> getQna() {

        List<Post> recentQna = postService.recentQna();

        List<AdminQnaDto> recentQnaList = recentQna.stream()
                .map(AdminQnaDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                recentQnaList
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
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                reportedPostList
        );
    }

    public record GetMembersResponseBody(@NonNull PageDto<AdminMemberDto> itemPage) {
    }

    @GetMapping(value = "/members/list")
    @Operation(summary = "회원 목록")
    public RsData<GetMembersResponseBody> getAllMembers(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<Member> members = memberService.findAll(pageable);

        Page<AdminMemberDto> memberPage = members.map(this::memberToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetMembersResponseBody(
                        new PageDto<>(memberPage)
                )
        );
    }

    private AdminMemberDto memberToDto(Member member) {
        AdminMemberDto dto = new AdminMemberDto(member);

        return dto;
    }

    public record GetNotesResponseBody(@NonNull PageDto<AdminSummaryNoteDto> itemPage) {
    }

    @GetMapping(value = "/summaryNotes/list")
    @Operation(summary = "요약노트 목록")
    public RsData<GetNotesResponseBody> getAllSummeryNotes(
            @RequestParam(defaultValue = "1") int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<SummaryNote> recentSummaryNotes = summaryNoteService.findAll(pageable);

        Page<AdminSummaryNoteDto> notePage = recentSummaryNotes.map(this::noteToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetNotesResponseBody(
                        new PageDto<>(notePage)
                )
        );
    }

    private AdminSummaryNoteDto noteToDto(SummaryNote summaryNote) {
        AdminSummaryNoteDto dto = new AdminSummaryNoteDto(summaryNote);

        return dto;
    }

    @PatchMapping("/posts/{postId}/report")
    @Operation(summary = "게시물 신고 취소")
    public RsData<Empty> cancelReport(@PathVariable("postId") Long id) {

        if (!postService.canCancelReport(postService.getPost(id))) {
            throw new GlobalException(CodeMsg.E400_6_CANCEL_REPORT_FAILED.getCode(), CodeMsg.E400_6_CANCEL_REPORT_FAILED.getMessage());
        }

        Member member = postService.getPost(id).getWriter();

        postService.deleteReport(id);

        if (postService.hasNotReported(member)){
            memberService.cancelReport(member);
        }

        return RsData.of(Msg.E200_7_CANCEL_REPORT_SUCCEED.getCode(),
                Msg.E200_7_CANCEL_REPORT_SUCCEED.getMsg());
    }

    public record GetAdmQnaResponseBody(@NonNull PageDto<AdminQnaDto> itemPage) {
    }

    @GetMapping(value = "/qna/list")
    @Operation(summary = "1대1 문의 목록")
    public RsData<GetAdmQnaResponseBody> getAllQna(
            @RequestParam(defaultValue = "1") int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<Post> qna = postService.findAllQna(pageable);

        Page<AdminQnaDto> qnaPage = qna.map(this::qnaToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetAdmQnaResponseBody(
                        new PageDto<>(qnaPage)
                )
        );
    }

    private AdminQnaDto qnaToDto(Post post) {
        AdminQnaDto dto = new AdminQnaDto(post);

        return dto;
    }

    public record getDeviceResponseBody(@NonNull Boolean isMobile){

    }

    @GetMapping("/deviceCheck")
    public RsData<getDeviceResponseBody> getDevice(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        boolean isMobile = userAgent.contains("android") || userAgent.contains("iphone") || userAgent.contains("ipad");

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), new getDeviceResponseBody(isMobile));
    }
}