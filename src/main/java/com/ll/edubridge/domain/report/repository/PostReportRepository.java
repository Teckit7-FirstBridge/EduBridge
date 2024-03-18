package com.ll.edubridge.domain.report.repository;

import com.ll.edubridge.domain.report.entity.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    // findById (단 변수가 들어있지 않는다면/postId 는 가능) 는  JPARepostiory 기본으로 탑재
    Optional<PostReport> findByPostId(Long postId);
}
