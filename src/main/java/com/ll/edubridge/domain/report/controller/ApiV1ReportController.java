package com.ll.edubridge.domain.report.controller;

import com.ll.edubridge.domain.report.dto.CreateReportDto;
import com.ll.edubridge.domain.report.dto.ReportDto;
import com.ll.edubridge.domain.report.entity.Report;
import com.ll.edubridge.domain.report.entity.ReportType;
import com.ll.edubridge.domain.report.service.ReportService;
import com.ll.edubridge.global.app.AppConfig;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rsData.RsData;
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
@RequestMapping(value = "/api/v1/report", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1ReportController", description = "신고 컨트롤러")
public class ApiV1ReportController {
    private final ReportService reportService;

    @PostMapping("/course/{courseId}")
    @Operation(summary = "강좌 신고 생성")
    public RsData<CreateReportDto> createCourseReport(@Valid @RequestBody CreateReportDto createReportDto, @PathVariable Long courseId) {
        Report report = reportService.create(createReportDto, ReportType.Course);

        CreateReportDto reportDto = new CreateReportDto(report);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(), reportDto);
    }

    @PostMapping("/post/{postId}")
    @Operation(summary = "글 신고 생성")
    public RsData<CreateReportDto> createPostReport(@Valid @RequestBody CreateReportDto createReportDto, @PathVariable Long postId) {
        Report report = reportService.create(createReportDto, ReportType.Post);

        CreateReportDto reportDto = new CreateReportDto(report);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(),
                Msg.E200_0_CREATE_SUCCEED.getMsg(), reportDto);
    }

    @GetMapping("/all")
    @Operation(summary = "전체 신고 목록 조회")
    public RsData<GetReportList> reportedAllList(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Report> reports = reportService.getAllReports(pageable);

        Page<ReportDto> reportPage = reports.map(this::getReportToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetReportList(new PageDto<>(reportPage))
        );
    }

    @GetMapping("/course")
    @Operation(summary = "강좌 신고 목록 조회")
    public RsData<GetReportList> reportedCourseList(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Report> reports = reportService.getReportsByMaterial(ReportType.Course, pageable);

        Page<ReportDto> reportPage = reports.map(this::getReportToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetReportList(new PageDto<>(reportPage))
        );
    }

    @GetMapping("/post")
    @Operation(summary = "글 신고 목록 조회")
    public RsData<GetReportList> reportedPostList(
            @RequestParam(defaultValue = "1") int page
    ) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Report> reports = reportService.getReportsByMaterial(ReportType.Post, pageable);

        Page<ReportDto> reportPage = reports.map(this::getReportToDto);

        return RsData.of(
                Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new GetReportList(new PageDto<>(reportPage))
        );
    }

    public record GetReportList(@NonNull PageDto<ReportDto> itemPage) {
    }

    private ReportDto getReportToDto(Report report) {
        ReportDto dto = new ReportDto(report);
        return dto;
    }
}
