package com.ll.edubridge.global.initData;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.global.app.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class All {
    private final MemberService memberService;

    @Value("${custom.prod.members.admin.password}")
    private String prodMemberAdminPassword;

    @Bean
    @Order(2)
    public ApplicationRunner initAll() {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) throws Exception {
                if (memberService.findByUsername("admin").isPresent()) return;

                String memberAdminPassword = AppConfig.isProd() ? prodMemberAdminPassword : "1234";

                Member memberAdmin = memberService.join("admin", memberAdminPassword).getData();
                memberAdmin.setRefreshToken("admin");
            }
        };
    }
}
