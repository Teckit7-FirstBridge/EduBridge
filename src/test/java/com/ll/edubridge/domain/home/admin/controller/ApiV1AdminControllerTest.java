package com.ll.edubridge.domain.home.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.global.exceptions.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



@WebMvcTest(ApiV1AdminController.class)
@AutoConfigureMockMvc
public class ApiV1AdminControllerTest { // fail....
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/admin/courses")
    @WithMockUser(username = "testUser", roles = "USER")
    void 비관리자_강좌등록시도() throws GlobalException {
        // Given
        CreateCourseDto createCourseDto = new CreateCourseDto();
        Course course = Course.builder()
                .title("title")
                .notice("notice")
                .imgUrl("img.png")
                .overView("overView")
                .price(1000)
                .build();
        when(courseService.create(any(CreateCourseDto.class))).thenReturn(course);

        // When
        GlobalException exception = assertThrows(GlobalException.class, () -> {
            courseService.create(createCourseDto);
        });

        //Then
        AssertionErrors.assertEquals(null, "403-1", exception.getRsData().getResultCode());
        AssertionErrors.assertEquals(null, "권한이 없습니다.", exception.getRsData().getMsg());
    }
}