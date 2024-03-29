package com.ll.edubridge.domain.report.service;

import com.ll.edubridge.domain.report.dto.CreateReportDto;
import com.ll.edubridge.domain.report.entity.Report;
import com.ll.edubridge.domain.report.entity.ReportType;
import com.ll.edubridge.domain.report.repository.ReportRepository;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    // 단건 조회용 - 현재 미사용
    public Report getReport(Long materialId, ReportType reportType) {
        Optional<Report> report = this.findByMaterialId(materialId, reportType);
        if (report.isPresent()) {
            return report.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(),CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    private Optional<Report> findByMaterialId(Long materialId, ReportType reportType) {
        return reportRepository.findByMaterialIdAndReportType(materialId, reportType);
    }
}
