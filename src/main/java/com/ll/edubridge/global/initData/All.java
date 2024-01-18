package com.ll.edubridge.global.initData;

import com.ll.edubridge.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class All {
    private final MemberService memberService;

    @Bean
    @Order(2)
    public ApplicationRunner initAll() {
        return args -> {
            if (memberService.findByUsername("system").isPresent()) return;

            memberService.join("system", "1234");
            memberService.join("admin", "1234");
        };
    }
}
