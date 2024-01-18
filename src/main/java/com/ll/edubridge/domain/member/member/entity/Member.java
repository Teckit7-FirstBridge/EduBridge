package com.ll.edubridge.domain.member.member.entity;

import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {

    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 72)
    private String password;

    @Column(length = 20)
    private String nickname;

    private int point = 0;

    private boolean report = false;

    @Column(unique = true)
    private String refreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<CourseEnroll> courseEnrollList;

    /*
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
     */
}
