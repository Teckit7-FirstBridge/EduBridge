package com.ll.edubridge.domain.member.member.controller;


import com.ll.edubridge.domain.member.member.dto.MemberDto;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiV1MemberController {
    private final MemberService memberService;
    private final Rq rq;

    public record LoginRequestBody(@NotBlank String username, @NotBlank String password) {
    }

    public record LoginResponseBody(@NonNull MemberDto item) {
    }

//    @PostMapping("/login")
//    public RsData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody body) {
//        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
//                body.username,
//                body.password
//        );
//
//        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().refreshToken());
//        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().accessToken());
//
//        return authAndMakeTokensRs.newDataOf(
//                new LoginResponseBody(
//                        new MemberDto(authAndMakeTokensRs.getData().member())
//                )
//        );
//    }


    public record MeResponseBody(@NonNull MemberDto item) {
    }

    @GetMapping("/me")
    public RsData<MeResponseBody> getMe() {
        return RsData.of("200-1","성공",
                new MeResponseBody(
                        new MemberDto(rq.getMember())
                )
        );
    }

    @PostMapping("/logout")
    public RsData<Empty> logout() {
        rq.setLogout();

        return RsData.of("200-1","성공");
    }
}