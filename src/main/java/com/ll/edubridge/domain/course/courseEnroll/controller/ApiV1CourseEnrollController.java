package com.ll.edubridge.domain.course.courseEnroll.controller;

import com.ll.edubridge.domain.course.courseEnroll.dto.CourseEnrollDto;
import com.ll.edubridge.domain.course.courseEnroll.dto.CreateCourseEnrollDto;
import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.domain.course.courseEnroll.repository.CourseEnrollRepository;
import com.ll.edubridge.domain.course.courseEnroll.service.CourseEnrollService;
import com.ll.edubridge.domain.course.summaryNote.controller.ApiV1SummaryNoteController;
import com.ll.edubridge.domain.course.summaryNote.dto.SummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.app.AppConfig;
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
@RequestMapping(value = "/api/v1/posts", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CourseEnrollController", description = "수업 테이블 컨트롤러")
public class ApiV1CourseEnrollController {
    private final CourseEnrollRepository courseEnrollRepository;
    private final CourseEnrollService courseEnrollService;
    private final Rq rq;

    @Getter
    public static class GetCourseEnrollResponsebody{
        @NonNull
        private final List<CourseEnrollDto> items;

        public GetCourseEnrollResponsebody(Page<CourseEnroll> page, Member currentUser) {
            this.items = page.getContent().stream()
                    .map(courseEnroll -> new CourseEnrollDto(courseEnroll))
                    .toList();
        }
    }

    @PostMapping("")
    @Operation(summary = "강의 요약 노트 등록")
    public RsData<CourseEnrollDto> create(@RequestBody CreateCourseEnrollDto createCourseEnrollDto) {
        CourseEnroll courseEnroll = courseEnrollService.create(rq.getMember(), createCourseEnrollDto);

        CourseEnrollDto courseEnrollDto = new CourseEnrollDto(courseEnroll);

        return RsData.of("200-0", "등록 성공", courseEnrollDto);

    }

    @GetMapping("")
    @Operation(summary = "수업 목록 조회")
    public RsData<ApiV1CourseEnrollController.GetCourseEnrollResponsebody> getSummaryNote(@RequestParam(value = "page", defaultValue = "1") int page){
        int pageSize = AppConfig.getBasePageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        Page<CourseEnroll> courseEnrolls = courseEnrollService.findAll(pageable);
        ApiV1CourseEnrollController.GetCourseEnrollResponsebody responseBody = new ApiV1CourseEnrollController.GetCourseEnrollResponsebody(courseEnrolls, rq.getMember());

        return RsData.of(
                "200-1",
                "성공",
                responseBody
        );
    }
}
