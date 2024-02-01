package com.ll.edubridge.domain.course.course.controller;

import com.ll.edubridge.domain.course.course.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ApiV1CourseControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CourseService courseService;

    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.?\\d{0,7}";

    @Test
    @DisplayName("GET /api/v1/courses")
    @WithMockUser(username = "testUser", roles = "USER")
    void 강좌조회_반환값확인() throws Exception {
        // WHEN
        MvcResult mvcResult = mvc
                .perform(get("/api/v1/courses"))
                .andDo(print())
                .andReturn();

        // THEN
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + content);
    }

    @Test
    @DisplayName("GET /api/v1/courses - Test Error Handling")
    @WithMockUser(username = "testUser", roles = "USER")
    void testErrorHandling() throws Exception {
        // WHEN
        MvcResult mvcResult = mvc
                .perform(get("/api/v1/courses")
                        .param("page", "-1"))  // Invalid page parameter to trigger an error
                .andDo(print())
                .andReturn();

        // THEN
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + content);

        ResultActions resultActions = MockMvcResultMatchers.jsonPath("$.errorCode", equalTo("401"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid page parameter")));

        // 이 부분이 추가되었습니다.
        resultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


//    @Test
//    @DisplayName("GET /api/v1/articles")
//    @WithMockUser(username = "testUser", roles = "USER")
//    void t1() throws Exception {
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(get("/api/v1/courses")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(ApiV1CourseController.class))
//                .andExpect(handler().methodName("getCourses"))
//                .andExpect(jsonPath("$.data.items[0].id", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.items[0].createDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.items[0].modifyDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.items[0].owner", notNullValue()))
//                .andExpect(jsonPath("$.data.items[0].title", notNullValue()))
//                .andExpect(jsonPath("$.data.items[0].imgUrl", notNullValue()))
//                .andExpect(jsonPath("$.data.items[0].price", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.items[0].notice", notNullValue()));
//    }

//    @Test
//    @DisplayName("GET /api/v1/courses/1")
//    @WithMockUser(username = "testUser", roles = "USER")
//    void t2() throws Exception {
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(get("/api/v1/courses/1"))
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(ApiV1CourseController.class))
//                .andExpect(handler().methodName("getCourse"))
//                .andExpect(jsonPath("$.data.items[0].id", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.items[0].createDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.items[0].modifyDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.items[0].owner", notNullValue()))
//                .andExpect(jsonPath("$.data.items[0].title", notNullValue()))
//                .andExpect(jsonPath("$.data.items[0].imgUrl", notNullValue()))
//                .andExpect(jsonPath("$.data.items[0].price", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.items[0].notice", notNullValue()));
//    }

//    @Test
//    @DisplayName("DELETE /api/v1/courses/1")
//    void t3() throws Exception {
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(delete("/api/v1/courses/1"))
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(ApiV1CourseController.class))
//                .andExpect(handler().methodName("removeCourse"))
//                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
//                .andExpect(jsonPath("$.data.item.title", notNullValue()))
//                .andExpect(jsonPath("$.data.item.body", notNullValue()));
//
//        Course course1 = courseService.findById(1L).orElse(null);
//        assertThat(course1).isNull();
//    }
//
//    @Test
//    @DisplayName("PUT /api/v1/courses/1")
//    void t4() throws Exception {
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(
//                        put("/api/v1/courses/1")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content("""
//                                        {
//                                            "title": "제목1-수정",
//                                            "body": "내용1-수정"
//                                        }
//                                        """)
//                )
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(ApiV1CourseController.class))
//                .andExpect(handler().methodName("modifyCourse"))
//                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
//                .andExpect(jsonPath("$.data.item.title", is("제목1-수정")))
//                .andExpect(jsonPath("$.data.item.body", is("내용1-수정")));
//    }
//
//    @Test
//    @DisplayName("POST /api/v1/courses")
//    @WithUserDetails("user1")
//    void t5() throws Exception {
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(
//                        post("/api/v1/courses")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content("""
//                                        {
//                                            "title": "제목 new",
//                                            "body": "내용 new"
//                                        }
//                                        """)
//                )
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(ApiV1CourseController.class))
//                .andExpect(handler().methodName("writeCourse"))
//                .andExpect(jsonPath("$.data.item.id", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.item.createDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.item.modifyDate", matchesPattern(DATE_PATTERN)))
//                .andExpect(jsonPath("$.data.item.authorId", instanceOf(Number.class)))
//                .andExpect(jsonPath("$.data.item.authorName", notNullValue()))
//                .andExpect(jsonPath("$.data.item.title", is("제목 new")))
//                .andExpect(jsonPath("$.data.item.body", is("내용 new")));
//    }
}