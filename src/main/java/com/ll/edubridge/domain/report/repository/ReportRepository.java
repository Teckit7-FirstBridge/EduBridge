package com.ll.edubridge.domain.report.repository;

import com.ll.edubridge.domain.report.entity.Report;
import com.ll.edubridge.domain.report.entity.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findReportsByReportType(ReportType reportType, Pageable pageable);
    List<Report> findTop5ByOrderByCreateDateDesc();

    Optional<Report> findReportById(Long id);

    Optional<List<Report>> findReportsByReportTypeAndMaterialId(ReportType reportType, Long materialId);
}
