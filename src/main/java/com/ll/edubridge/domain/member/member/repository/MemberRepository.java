package com.ll.edubridge.domain.member.member.repository;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByRefreshToken(String refreshToken);

    List<Member> findAll();

    List<Member> findTop5ByOrderByIdDesc();

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.notifications n WHERE m.id = :memberId ORDER BY n.createDate DESC")
    Optional<Member> findMemberWithRecentNotifications(@Param("memberId") Long memberId);
}
