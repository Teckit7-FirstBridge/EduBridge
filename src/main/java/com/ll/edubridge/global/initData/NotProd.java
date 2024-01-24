package com.ll.edubridge.global.initData;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

//@Configuration
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    @Order(3)
    public ApplicationRunner initNotProd() {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) {
                if (memberService.findByUsername("user1").isPresent()) return;

                Member memberUser1 = memberService.join("user1", "1234").getData();
                memberUser1.setRefreshToken("user1");

                Member memberUser2 = memberService.join("user2", "1234").getData();
                memberUser2.setRefreshToken("user2");

                Member memberUser3 = memberService.join("user3", "1234").getData();
                memberUser3.setRefreshToken("user3");

                Member memberUser4 = memberService.join("user4", "1234").getData();
                memberUser4.setRefreshToken("user4");

                postService.write(memberUser1, "제목 1", "내용 1", true);
                postService.write(memberUser1, "제목 2", "내용 2", true);
                postService.write(memberUser1, "제목 3", "내용 3", false);
                postService.write(memberUser1, "제목 4", "내용 4", true);

                postService.write(memberUser2, "제목 5", "내용 5", true);
                postService.write(memberUser2, "제목 6", "내용 6", false);

                IntStream.rangeClosed(7, 150).forEach(i -> {
                    postService.write(memberUser3, "제목 " + i, "내용 " + i, true);
                });
            }
        };
    }
}
