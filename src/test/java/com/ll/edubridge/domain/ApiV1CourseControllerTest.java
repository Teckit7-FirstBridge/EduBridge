package com.ll.edubridge.domain;

import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.global.exceptions.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AssertionErrors;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ApiV1CourseControllerTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("GET /api/v1/courses/{courseId}")
    @WithMockUser(username = "testUser", roles = "USER")
    void 없는강좌조회() {
        // Given
        Long courseId = 123L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When
        GlobalException exception = assertThrows(GlobalException.class, () -> {
            courseService.getCourse(courseId);
        });

        //Then
        AssertionErrors.assertEquals(null, "404-1", exception.getRsData().getResultCode());
        AssertionErrors.assertEquals(null, "해당 데이터를 찾을 수 없습니다.", exception.getRsData().getMsg());
    }
}