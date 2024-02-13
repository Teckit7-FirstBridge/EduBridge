package com.ll.edubridge.domain.home.admin.controller;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
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
import com.ll.edubridge.domain.post.post.repository.PostRepository;
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
    private final PostRepository postRepository;

    private final Rq rq;

    @GetMapping(value = "/courses")
    @Operation(summary = "강좌 최신순")
    public RsData<List<RecentCourseDto>> getRecentCourses() {

        List<Course> recentCourses = courseService.findRecentCourse();

        List<RecentCourseDto> courseList = recentCourses.stream()
                .map(RecentCourseDto::new)
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
    public RsData<List<RecentMemberDto>> getMembers() {

        List<Member> recentMembers = memberService.recentMembers();

        List<RecentMemberDto> recentMemberList = recentMembers.stream()
                .map(RecentMemberDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                recentMemberList
        );
    }

    @GetMapping(value = "/summaryNotes")
    @Operation(summary = "최신 요약노트")
    public RsData<List<RecentSummaryNoteDto>> getSummeryNotes() {

        List<SummaryNote> recentSummaryNotes = summaryNoteService.recentSummaryNotes();

        List<RecentSummaryNoteDto> recentSummaryNoteList = recentSummaryNotes.stream()
                .map(RecentSummaryNoteDto::new)
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

    public record GetMembersResponseBody(@NonNull PageDto<RecentMemberDto> itemPage) {
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

        Page<RecentMemberDto> memberPage = members.map(this::memberToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetMembersResponseBody(
                        new PageDto<>(memberPage)
                )
        );
    }

    private RecentMemberDto memberToDto(Member member) {
        RecentMemberDto dto = new RecentMemberDto(member);

        return dto;
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

        Page<RecentSummaryNoteDto> notePage = recentSummaryNotes.map(this::noteToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetNotesResponseBody(
                        new PageDto<>(notePage)
                )
        );
    }

    private RecentSummaryNoteDto noteToDto(SummaryNote summaryNote) {
        RecentSummaryNoteDto dto = new RecentSummaryNoteDto(summaryNote);

        return dto;
    }

    @PostMapping("/{courseId}/videos")
    @Operation(summary = "강의 등록")
    public RsData<CreateVideoDto> createVideo(@PathVariable("courseId") Long courseId,
                                              @Valid @RequestBody CreateVideoDto createVideoDto) {

        if (!videoService.haveAuthority())
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Video video = videoService.create(createVideoDto);
        CreateVideoDto createdVideoDto = new CreateVideoDto(video);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(),
                createdVideoDto);
    }

    @PutMapping("/{courseId}/startOrStop")
    @Operation(summary = "강좌 공개 or 비공개")
    public RsData<CourseDto> startOrStopCourse(@PathVariable("courseId") Long courseId){
        Course course = courseService.startOrstop(courseId);
        CourseDto courseDto = new CourseDto(course,rq.getMember());
        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),Msg.E200_2_MODIFY_SUCCEED.getMsg(),courseDto);
    }

    @PutMapping("/{courseId}/videos/{id}")
    @Operation(summary = "강의 수정")
    public RsData<VideoDto> modifyVideo(@PathVariable("courseId") Long courseId,
                                        @PathVariable("id") Long id,
                                        @RequestBody VideoDto videoDto) {

        if (!videoService.haveAuthority())
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Course course = courseService.getCourse(courseId);
        if (course == null) {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }

        Video video = videoService.findByCourseIdAndId(courseId, id);
        if (video == null) {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }

        Video modifyVideo = videoService.modify(id, videoDto);

        VideoDto modifyVideoDto = new VideoDto(modifyVideo);

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg(),
                modifyVideoDto);
    }

    @DeleteMapping("/{courseId}/videos/{id}")
    @Operation(summary = "강의 삭제")
    public RsData<Empty> deleteVideo(@PathVariable("courseId") Long courseId,
                                     @PathVariable("id") Long id) {

        if (!videoService.haveAuthority())
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Course course = courseService.getCourse(courseId);
        if (course == null) {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }

        Video video = videoService.findByCourseIdAndId(courseId, id);
        if (video == null) {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }

        videoService.delete(id);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
    }

    @PostMapping("/courses")
    @Operation(summary = "강좌 등록")
    public RsData<CreateCourseDto> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {

        if (!courseService.haveAuthority())
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Course course = courseService.create(createCourseDto);

        CreateCourseDto createdCourseDto = new CreateCourseDto(course);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(),
                createdCourseDto);
    }

    @PutMapping("/courses/{id}")
    @Operation(summary = "강좌 수정")
    public RsData<CourseDto> modifyCourse(
            @PathVariable("id") Long id,
            @RequestBody CourseDto courseDto) {

        if (!courseService.haveAuthority())
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Course modifyCourse = courseService.modify(id, courseDto);

        CourseDto modifyCourseDto = new CourseDto(modifyCourse,rq.getMember());

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg(), modifyCourseDto);
    }

    @DeleteMapping("/courses/{id}")
    @Operation(summary = "강좌 삭제")
    public RsData<Empty> deleteCourse(@PathVariable("id") Long id) {

        if (!courseService.haveAuthority())
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        courseService.delete(id);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
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

    public record GetQnaResponseBody(@NonNull PageDto<AdminQnaDto> itemPage) {
    }

    @GetMapping(value = "/qna/list")
    @Operation(summary = "1대1 문의 목록")
    public RsData<GetQnaResponseBody> getAllQna(
            @RequestParam(defaultValue = "1") int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<Post> qna = postService.findAllQna(pageable);

        Page<AdminQnaDto> qnaPage = qna.map(this::qnaToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetQnaResponseBody(
                        new PageDto<>(qnaPage)
                )
        );
    }

    private AdminQnaDto qnaToDto(Post post) {
        AdminQnaDto dto = new AdminQnaDto(post);

        return dto;
    }
}