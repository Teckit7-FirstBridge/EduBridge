package com.ll.edubridge.domain.course.course.entity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/api/v1/courses/{course-id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1CourseDetailController", description = "글 CRUD 컨트롤러")
@SecurityRequirement(name = "bearerAuth") // 옵션

public class ApiV1CourseDetailController {
    @Operation(summary = "글 리스트")


}
