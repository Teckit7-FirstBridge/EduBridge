package com.ll.edubridge.domain.course.course.entity;

import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Course extends BaseEntity {

    @Column(length = 200)
    private String title;

    @Column(length = 1000)
    private String notice;

    @Column(length = 150)
    private String imgUrl;

    @Column(length = 500)
    private String overView;

    @ManyToOne(optional = false)
    private Member owner;

    private int price;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Video> videoList;

    @ManyToMany
    Set<Member> voter;
}
