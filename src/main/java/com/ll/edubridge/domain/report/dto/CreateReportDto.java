package com.ll.edubridge.domain.report.dto;

import com.ll.edubridge.domain.report.entity.Report;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class CreateReportDto {
    @NonNull
    private String reportReason;
    @NonNull
    private Long materialId;

    public CreateReportDto(Report report) {
        this.reportReason = report.getReportReason();
        this.materialId = report.getMaterialId();
    }
}
