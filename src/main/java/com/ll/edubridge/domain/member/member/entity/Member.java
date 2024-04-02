package com.ll.edubridge.domain.member.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.edubridge.domain.CommentVoter.entity.CommentVoter;
import com.ll.edubridge.domain.CourseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.PostVoter.entity.PostVoter;
import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import com.ll.edubridge.standard.util.Ut;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity { // 보안이 들어있는 클래스

    private int registerCount;

    @Column(unique = true, length = 100)
    private String username;

    @Column(length = 72)
    private String password;

    @Column(unique = true, length = 50)
    private String nickname;

    private int point;

    private boolean report;

    @Column(unique = true)
    private String refreshToken;

    private String profileImgUrl;

    private boolean visitedToday;

    @Builder.Default
    private int dailyGoal = 3;

    private int dailyAchievement;

    private String uuid;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<CourseVoter> courseVoters;

    @OneToMany(mappedBy = "member" , cascade = CascadeType.REMOVE)
    private Set<PostVoter> postVoters;

    @OneToMany(mappedBy = "member" , cascade = CascadeType.REMOVE)
    private Set<CommentVoter> commentVoters;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CourseEnroll> courseEnrollList;


    public String getProfileImgUrlOrDefault() {
        return Ut.str.hasLength(profileImgUrl) ? profileImgUrl : "https://placehold.co/640x640?text=O_O";
    }


    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Transient
    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        authorities.add("ROLE_MEMBER");

        if (List.of("system", "admin").contains(username)) {
            authorities.add("ROLE_ADMIN");
        }

        return authorities;
    }

    public String getName() {
        return username;
    }

}
