package com.ll.edubridge.domain.report.dto;

import com.ll.edubridge.domain.report.entity.Report;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
public class ReportDto {
    @NonNull
    private Long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String reportReason;
    @NonNull
    private Long materialId;

    public ReportDto(Report report) {
        this.id = report.getId();
        this.createDate = report.getCreateDate();
        this.reportReason = report.getReportReason();
        this.materialId = report.getMaterialId();
    }

    public ReportDto() {}
}
