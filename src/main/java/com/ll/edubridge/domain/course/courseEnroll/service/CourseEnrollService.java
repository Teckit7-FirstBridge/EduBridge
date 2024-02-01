package com.ll.edubridge.domain.course.courseEnroll.service;

import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.domain.course.courseEnroll.repository.CourseEnrollRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.repository.MemberRepository;
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
public class CourseEnrollService {
    private final Rq rq;
    private final CourseEnrollRepository courseEnrollRepository;
    private final CourseService courseService;
    private final MemberRepository memberRepository;

    public Page<CourseEnroll> findAll(Pageable pageable) {
        return courseEnrollRepository.findAll(pageable);
    }

    public Optional<CourseEnroll> findById(Long id) {
        return courseEnrollRepository.findById(id);
    }

    @Transactional
    public CourseEnroll create(Member member, Long courseId,int point,int price) {
        CourseEnroll courseEnroll = CourseEnroll.builder()
                .course(courseService.getCourse(courseId))
                .member(member)
                .build();
        member.setPoint(point - price);
        memberRepository.save(member); // member의 정보를 저장
        return courseEnrollRepository.save(courseEnroll);
    }

    @Transactional
    public CourseEnroll getCourseEnroll(Long id) {
        Optional<CourseEnroll> courseEnroll = this.findById(id);
        if (courseEnroll.isPresent()) {
            return courseEnroll.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(),CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    @Transactional
    public boolean haveAuthority(Long id) {
        Member member = rq.getMember();

        if (member == null) return false;

        if (rq.isAdmin()) return true;

        return true;
    }






}
