package com.ll.edubridge.domain.report.service;

import com.ll.edubridge.domain.report.dto.CreateReportDto;
import com.ll.edubridge.domain.report.entity.Report;
import com.ll.edubridge.domain.report.entity.ReportType;
import com.ll.edubridge.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;

    @Transactional
    public Report create(CreateReportDto createReportDto, ReportType reportType) {
        Report report = Report.builder()
                .reportReason(createReportDto.getReportReason())
                .reportType(reportType)
                .materialId(createReportDto.getMaterialId())
                .build();

        return reportRepository.save(report);
    }

    public Page<Report> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    public Page<Report> getReportsByMaterial(ReportType reportType, Pageable pageable) {
        return reportRepository.findReportsByReportType(reportType, pageable);
    }

    public List<Report> recentReport() {
        return reportRepository.findTop5ByOrderByCreateDateDesc();
    }
}
