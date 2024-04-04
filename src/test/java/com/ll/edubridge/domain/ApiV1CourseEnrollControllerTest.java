package com.ll.edubridge.domain;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.course.courseEnroll.controller.ApiV1CourseEnrollController;
import com.ll.edubridge.domain.course.courseEnroll.service.CourseEnrollService;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.point.point.service.PointService;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AssertionErrors;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiV1CourseEnrollControllerTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private CourseService courseService;

    @Mock
    private CourseEnrollService courseEnrollService;

    @Mock
    private PointService pointService;

    @Mock
    private Rq rq;

    @Test
    @DisplayName("GET /api/v1/enroll/{courseId}")
    @WithMockUser(username = "testUser", roles = "USER")
    void 수강등록_포인트부족() {
        // Given
        Course mockCourse = Course.builder()
                .title("title")
                .notice("notice")
                .imgUrl("img.png")
                .overView("overView")
                .price(1000)
                .build();
        Member mockMember = Member.builder()
                .username("testUser")
                .point(100)
                .build();
        when(courseRepository.findById(123L)).thenReturn(Optional.of(mockCourse));
        when(memberService.findByUsername("testUser")).thenReturn(Optional.ofNullable(mockMember));

        ApiV1CourseEnrollController apiController = new ApiV1CourseEnrollController(courseEnrollService, courseService, rq, memberService);
        when(rq.getMember()).thenReturn(mockMember);
        when(courseService.getCourse(123L)).thenReturn(mockCourse);

        // When
        GlobalException exception = assertThrows(GlobalException.class, () -> {
            apiController.create(123L);
        });

        //Then
        AssertionErrors.assertEquals(null, "400-1", exception.getRsData().getResultCode());
        AssertionErrors.assertEquals(null, "등록 실패", exception.getRsData().getMsg());
    }
}