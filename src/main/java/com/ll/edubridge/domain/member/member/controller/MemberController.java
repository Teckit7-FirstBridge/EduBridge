package com.ll.edubridge.domain.member.member.controller;

import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;


    @GetMapping("/socialLogin/{providerTypeCode}")
    @Operation(summary = "소셜로그인")
    public String socialLogin(String redirectUrl, @PathVariable("providerTypeCode") String providerTypeCode) {
        if (rq.isFrontUrl(redirectUrl)) { // 돌아가야되는 주소가 Front 그래서 쿠키를 남김
            rq.setCookie("redirectUrlAfterSocialLogin",redirectUrl, 60 * 10);
        }               // 소셜로그인 성공나서 돌아가야할 위치가 여기고 유효기간은 10분

        // /oauth2/authorization/kakao
        // client 갔다가 옴 즉 전처리함
        // -> 왜? 스벨트 킷 (프론트 내에서) 방식으로 카카오로그인을 하기 때문
        // 즉 프론트에서 원래 사용자가 위치한 곳으로 돌아가기 위해서 설정
        return "redirect:/oauth2/authorization/" + providerTypeCode;
    }


}
