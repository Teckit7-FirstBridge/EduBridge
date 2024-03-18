package com.ll.edubridge.domain.report.entity;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PostReport extends BaseEntity {

    @Column(length = 500)
    private String content;

    @Column()
    private Long postId; // 신고 당한 글 번호 , postId을 통해 신고 당한 글을 조회 하기 위해서

    @ManyToOne(optional = false) // 외래키 / NotNull
    // optional = false : 반드시 Member 엔티티를 참조해야 하며, null 될 수없음을 명시.
    private Member writer;
    // 자료형이 Member 을 쓴 이유 : Member 엔티티를 참조하여 ,
    // 이 필드가 데이터베이스 내에서 다른 테이블의 특정 행과 관계를 맺기 위해서
    // Member 클래스를 사용함으로써,
    // 실세계에서 '작성자'가 가지고 있는 속성과 행동을 코드 내에서 Member 객체로 표현할 수 있다.
}
