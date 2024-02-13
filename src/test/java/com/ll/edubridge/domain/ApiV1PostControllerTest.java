package com.ll.edubridge.domain;

import com.ll.edubridge.domain.post.post.repository.PostRepository;
import com.ll.edubridge.domain.post.post.service.PostService;
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
public class ApiV1PostControllerTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("GET /api/v1/courses/{courseId}")
    @WithMockUser(username = "testUser", roles = "USER")
    void 없는게시물조회() {
        // Given
        Long postId = 123L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When
        GlobalException exception = assertThrows(GlobalException.class, () -> {
            postService.getPost(postId);
        });

        //Then
        AssertionErrors.assertEquals(null, "404-1", exception.getRsData().getResultCode());
        AssertionErrors.assertEquals(null, "해당 데이터를 찾을 수 없습니다.", exception.getRsData().getMsg());
    }
}