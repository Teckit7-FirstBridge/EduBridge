package com.ll.edubridge.domain.member.member.repository;

import com.ll.edubridge.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByRefreshToken(String refreshToken);

    List<Member> findAll();

    List<Member> findTopByCreateDate(int num);
}
