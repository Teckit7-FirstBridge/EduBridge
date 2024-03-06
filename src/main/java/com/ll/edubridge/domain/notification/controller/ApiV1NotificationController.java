package com.ll.edubridge.domain.notification.controller;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.notification.entity.Notification;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class ApiV1NotificationController {
    private final Rq rq;
    private final NotificationService notificationService;



    @GetMapping("/get")
    public RsData<List<Notification>> getNotification(){
        Member member = rq.getMember();

        List<Notification> byMemberId = notificationService.findByMemberId(member.getId());
        return RsData.of( Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),byMemberId);
    }
}
