package com.ll.edubridge.domain;

import com.ll.edubridge.domain.course.video.repository.VideoRepository;
import com.ll.edubridge.domain.course.video.service.VideoService;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiV1VideoControllerTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    @Test
    @DisplayName("GET /api/v1/courses/{courseId}/videos/{videoId}")
    @WithMockUser(username = "testUser", roles = "USER")
    void 없는강의조회() {
        // Given
        Long id = 123L;
        when(videoRepository.findById(id)).thenReturn(null);

        // When
        GlobalException exception = assertThrows(GlobalException.class, () -> {
            videoService.getVideo(id);
        });

        //Then
        AssertionErrors.assertEquals(null, "404-1", exception.getRsData().getResultCode());
        AssertionErrors.assertEquals(null, "해당 데이터를 찾을 수 없습니다.", exception.getRsData().getMsg());
    }

}