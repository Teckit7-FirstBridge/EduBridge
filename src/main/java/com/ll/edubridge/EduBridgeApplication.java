package com.ll.edubridge;

import com.ll.edubridge.domain.youtube.PythonScriptService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class EduBridgeApplication {

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        // 시간대를 'Asia/Seoul'로 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        PythonScriptService.installPython();

    }

    public static void main(String[] args) {
        SpringApplication.run(EduBridgeApplication.class, args);
    }

}