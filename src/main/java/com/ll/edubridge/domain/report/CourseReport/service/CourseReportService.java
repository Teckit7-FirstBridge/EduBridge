package com.ll.edubridge.domain.report.CourseReport.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.domain.report.CourseReport.dto.CreateCourseReportDto;
import com.ll.edubridge.domain.report.CourseReport.entity.CourseReport;
import com.ll.edubridge.domain.report.CourseReport.repository.CourseReportRepository;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseReportService {

    private final CourseReportRepository courseReportRepository;
    private final Rq rq;


    @Transactional
    public CourseReport create(Member member, CreateCourseReportDto createCourseReportDto, Long courseId) {
        CourseReport courseReport = CourseReport.builder()
                .content(createCourseReportDto.getContent())
                .writer(member)
                .courseId(courseId)
                .build();

        return courseReportRepository.save(courseReport);
    }

    public boolean haveAuthority(Long courseId) {
        Member member = rq.getMember();

        CourseReport courseReport  = this.getCourseReport(courseId);

        if (member == null) return false;

        if (rq.isAdmin()) return true;

        return courseReport.getWriter().equals(member);
    }

    public CourseReport getCourseReport(Long courseId) {
        Optional<CourseReport> courseReport = this.findBycourseId(courseId);
        if (courseReport.isPresent()) {
            return courseReport.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(),CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    private Optional<CourseReport> findBycourseId(Long courseId) {
        return courseReportRepository.findByCourseId(courseId);
    }

    @Transactional
    public void delete(Long courseId) {
        CourseReport courseReport = this.getCourseReport(courseId);
        courseReportRepository.delete(courseReport);
    }

    public Page<CourseReport> getCourseReports(Pageable pageable) {
        Member member = rq.getMember();

        return courseReportRepository.findBypostId(member, pageable);
    }
}
