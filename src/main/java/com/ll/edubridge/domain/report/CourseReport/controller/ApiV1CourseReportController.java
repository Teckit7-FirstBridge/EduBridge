package com.ll.edubridge.domain.report.CourseReport.controller;

import com.ll.edubridge.domain.post.post.dto.PostDto;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.report.CourseReport.dto.CourseReportDto;
import com.ll.edubridge.domain.report.CourseReport.dto.CreateCourseReportDto;
import com.ll.edubridge.domain.report.CourseReport.entity.CourseReport;
import com.ll.edubridge.domain.report.CourseReport.service.CourseReportService;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/courseReport", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CourseReportController", description = "강좌 신고 컨트롤러")
public class ApiV1CourseReportController {
    private final CourseReportService courseReportService;
    private final Rq rq;

    @PostMapping("/{courseId}")
    @Operation(summary = "강좌 신고")
    public RsData<CreateCourseReportDto> createCourseReport(@Valid @RequestBody CreateCourseReportDto CreateCourseReportDto, @PathVariable Long courseId) {
        CourseReport courseReport = courseReportService.create(rq.getMember(), CreateCourseReportDto, courseId);

        CreateCourseReportDto reportDto = new CreateCourseReportDto(courseReport);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(), reportDto);
    }

    @DeleteMapping("/{courseId}")
    @Operation(summary = "신고글 삭제")
    public RsData<Empty> delete(@PathVariable("courseId") Long courseId) {

        if (!courseReportService.haveAuthority(courseId))
            throw new GlobalException(CodeMsg.E403_1_NO.getCode(),CodeMsg.E403_1_NO.getMessage());

        courseReportService.delete(courseId);

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
    }

    @GetMapping("/courseReportList")
    @Operation(summary = "강좌 신고 목록")
    public RsData<GetCourseReportList> courseReportList(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<CourseReport> courseReports = courseReportService.getCourseReports(pageable);

        Page<CourseReportDto> courseReportPage = courseReports.map(this::getCourseReportToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetCourseReportList(new PageDto<>(courseReportPage))
        );
    }

    public record GetCourseReportList(@NonNull PageDto<CourseReportDto> itemPage) {
    }

    private CourseReportDto getCourseReportToDto(CourseReport courseReport) {
        CourseReportDto dto = new CourseReportDto(courseReport, rq.getMember());

        return dto;
    }
}
