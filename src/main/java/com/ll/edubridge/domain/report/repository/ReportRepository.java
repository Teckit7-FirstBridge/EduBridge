package com.ll.edubridge.domain.report.repository;

import com.ll.edubridge.domain.report.entity.Report;
import com.ll.edubridge.domain.report.entity.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByMaterialIdAndReportType(Long materialId, ReportType reportType);

    Page<Report> findReportsByReportType(ReportType reportType, Pageable pageable);
}
