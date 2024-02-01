package com.ll.edubridge.domain.course.course.controller;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.service.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(ApiV1CourseController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ApiV1CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;


    @Test
    void getPosts() throws Exception{



    }

    @Test()
    void getCourse() throws Exception{

    }

    @Test
    void createCourse() {
    }

    @Test
    void modify() {
    }

    @Test
    void delete() {
    }
}