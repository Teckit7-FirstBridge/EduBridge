package com.ll.edubridge.domain.course.roadmap.entity;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Roadmap extends BaseEntity {

    @Column(length = 50)
    private String title;

    @Column(length = 300)
    private String overView;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE)
    private List<Course> curriculum;

    @Column
    private String hashtags;

    @Column
    private String owner;
}
