package com.ll.edubridge.domain.notification.controller;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.notification.entity.Notification;
import com.ll.edubridge.domain.notification.entity.NotificationDto;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.domain.post.post.dto.PostDto;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class ApiV1NotificationController {
    private final Rq rq;
    private final NotificationService notificationService;
    private final MemberService memberService;


    public record GetNotificationResponseBody(@NonNull List<NotificationDto> dtoList) {
    }

    @GetMapping("/get")
    public RsData<GetNotificationResponseBody> getNotification(){
        Member member = rq.getMember();
        List<Notification> byMemberId = notificationService.findByMemberId(member.getId());
        List<NotificationDto> notificationDtoList = byMemberId.stream()
                .map(notification -> {
                    if(notification.getSender_id() == null){
                        return new NotificationDto(notification,member.getNickname(),null);
                    }else{
                        return new NotificationDto(notification,member.getNickname(),memberService.getMember(notification.getSender_id()).getNickname());
                    }
                })
                .collect(Collectors.toList());
        return RsData.of( Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),new GetNotificationResponseBody(notificationDtoList));
    }

    @GetMapping("/isAlarm")
    public RsData<Boolean> isAlarm(){
        Member member = rq.getMember();

        boolean isAlarm = notificationService.isAlarm(member);
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(),isAlarm);
    }

    @PutMapping("/read/{id}")
    public RsData<GetNotificationResponseBody> readNotification(@PathVariable("id") Long id){
        notificationService.readNoti(id);
        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),Msg.E200_2_MODIFY_SUCCEED.getMsg(),null);
    }

}
