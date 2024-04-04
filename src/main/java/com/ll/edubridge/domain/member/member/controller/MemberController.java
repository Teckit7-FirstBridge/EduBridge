package com.ll.edubridge.domain.member.member.controller;

import com.ll.edubridge.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final Rq rq;

    @GetMapping("/socialLogin/{providerTypeCode}")
    @Operation(summary = "소셜로그인")
    public String socialLogin(String redirectUrl, @PathVariable("providerTypeCode") String providerTypeCode) {
        if (rq.isFrontUrl(redirectUrl)) {
            rq.setCookie("redirectUrlAfterSocialLogin",redirectUrl, 60 * 10);
        }

        return "redirect:/oauth2/authorization/" + providerTypeCode;
    }
}
