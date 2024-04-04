package com.ll.edubridge.domain.course.course.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CourseAuthDto {

    private boolean isEnroll;

    public CourseAuthDto(Boolean isEnroll){
        this.isEnroll=isEnroll;
    }

}
