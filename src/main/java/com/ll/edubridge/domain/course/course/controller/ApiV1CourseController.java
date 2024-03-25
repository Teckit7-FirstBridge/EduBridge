package com.ll.edubridge.domain.course.course.controller;


import com.ll.edubridge.domain.CourseVoter.service.CourseVoterService;
import com.ll.edubridge.domain.course.course.dto.CourseAuthDto;
import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.domain.course.courseEnroll.service.CourseEnrollService;
import com.ll.edubridge.domain.home.admin.dto.AdminCourseEnrollDto;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import com.ll.edubridge.standard.base.KwTypeCourse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final MemberService memberService;
    private final CourseVoterService courseVoterService;

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

    @PostMapping("/write")
    @Operation(summary = "강좌 등록")
    public RsData<CreateCourseDto> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {
        Course course;

        if (memberService.canEnroll(rq.getMember())){
            course = courseService.create(createCourseDto);
            memberService.increaseRegisterCount();
        } else {
            throw new GlobalException(CodeMsg.E400_9_COUNT_ALREADY_FULL.getCode(), CodeMsg.E400_9_COUNT_ALREADY_FULL.getMessage());
        }

        CreateCourseDto createdCourseDto = new CreateCourseDto(course);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(),
                createdCourseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "강좌 수정")
    public RsData<CourseDto> modifyCourse(
            @PathVariable("id") Long id,
            @RequestBody CreateCourseDto courseDto) {

        Course course = courseService.getCourse(id);

        if (!courseService.haveAuthority(course.getWriter().getId()))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        Course modifyCourse = courseService.modify(id, courseDto);

        CourseDto modifyCourseDto = new CourseDto(modifyCourse,rq.getMember());

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg(), modifyCourseDto);
    }

    @DeleteMapping("/{id}/{writer_id}")
    @Operation(summary = "강좌 삭제")
    public RsData<Empty> deleteCourse(@PathVariable("id") Long id,@PathVariable Long writer_id) {

        if (!courseService.haveAuthority(writer_id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        courseService.delete(id);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
    }


    @GetMapping(value = "/{courseId}/enroll/{writerId}")
    @Operation(summary = "강좌별 수강생 목록")
    public RsData<List<AdminCourseEnrollDto>> getEnrollByCourseId(
            @PathVariable("courseId") Long courseId,
            @PathVariable Long writerId) {

        if (!courseService.haveAuthority(writerId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());

        List<CourseEnroll> courseEnrolls = courseEnrollService.findByCourseId(courseId);

        List<AdminCourseEnrollDto> enrollList = courseEnrolls.stream()
                .map(AdminCourseEnrollDto::new)
                .toList();

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                enrollList
        );
    }

    @GetMapping(value = "")
    @Operation(summary = "강좌 다건 조회")
    public RsData<GetCoursesResponsebody> getCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeCourse kwType
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<Course> coursePage;
        if (rq.isAdmin()) {
            coursePage = courseService.findByKwAdmin(kwType, kw, null, pageable);
        } else {
            coursePage = courseService.findByKw(kwType, kw, null, pageable);
        }

        GetCoursesResponsebody responseBody = new GetCoursesResponsebody(coursePage);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                responseBody

        );
    }

    @GetMapping(value = "/mycourse")
    @Operation(summary = "내가 등록한 강좌 조회")
    public RsData<GetCoursesResponsebody> getMyCourse(@RequestParam(defaultValue = "1") int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));

        Page<Course> coursePage;
        coursePage = courseService.findMyCourse(rq.getMember(),pageable);


        GetCoursesResponsebody responseBody = new GetCoursesResponsebody(coursePage);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                responseBody

        );
    }

    @PutMapping("/{courseId}/startOrStop/{writer_id}")
    @Operation(summary = "강좌 공개 or 비공개")
    public RsData<CourseDto> startOrStopCourse(@PathVariable("courseId") Long courseId,@PathVariable Long writer_id){
        if (!courseService.haveAuthority(writer_id))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(), CodeMsg.E403_1_NO.getMessage());
        Course course = courseService.getCourse(courseId);
        if(course.getVideoList().size()<=AppConfig.videoMinNum){
            throw new GlobalException(CodeMsg.E400_10_VIDEO_LESS_THAN_5_CANNOT_PUBLISH.getCode(),CodeMsg.E400_10_VIDEO_LESS_THAN_5_CANNOT_PUBLISH.getMessage());
        }
        course = courseService.startOrStop(course);
        CourseDto courseDto = new CourseDto(course,rq.getMember());
        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),Msg.E200_2_MODIFY_SUCCEED.getMsg(),courseDto);
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
        Course course = courseService.getCourse(id);
        if (!courseVoterService.canLike(member,course)) {
            throw new GlobalException(CodeMsg.E400_1_ALREADY_RECOMMENDED.getCode(),CodeMsg.E400_1_ALREADY_RECOMMENDED.getMessage());
        }
        courseVoterService.vote( member,course);

        return RsData.of(Msg.E200_4_RECOMMEND_SUCCEED.getCode(),
                Msg.E200_4_RECOMMEND_SUCCEED.getMsg());
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "강좌 좋아요 취소")
    public RsData<Void> deleteVote(@PathVariable("id") Long id) {
        Member member = rq.getMember();
        Course course = courseService.getCourse(id);

        if (!courseVoterService.canCancelLike(member, course)) {
            throw new GlobalException(CodeMsg.E400_2_NOT_RECOMMENDED_YET.getCode(),CodeMsg.E400_2_NOT_RECOMMENDED_YET.getMessage());
        }

        courseVoterService.deleteVote(course, member);

        return RsData.of(Msg.E200_5_CANCEL_RECOMMEND_SUCCEED.getCode(),
                Msg.E200_5_CANCEL_RECOMMEND_SUCCEED.getMsg());
    }
}