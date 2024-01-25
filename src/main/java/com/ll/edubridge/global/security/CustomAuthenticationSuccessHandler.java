package com.ll.edubridge.global.security;


import com.ll.edubridge.domain.member.member.service.AuthTokenService;
import com.ll.edubridge.global.rq.Rq;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final Rq rq;
    private final AuthTokenService authTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String redirectUrlAfterSocialLogin = rq.getCookieValue("redirectUrlAfterSocialLogin", "");

        // 일반적인 방식이 아닌 외부 프론트도메인에서 요청한
        // 소셜 로그인인 경우에는 아래와 같이 처리한다.
        if (rq.isFrontUrl(redirectUrlAfterSocialLogin)) {
            String accessToken = authTokenService.genAccessToken(rq.getMember());
            String refreshToken = rq.getMember().getRefreshToken();

            rq.destroySession(); // 기본적으로 세션에 회원정보를 넣어준다. 이 경우에는 그럴 필요가 없어서 지운다.
            rq.setCrossDomainCookie("accessToken", accessToken); // 쿠키 회원 정보를 넣기 위해서 필요 !! (id,username, authorities["ROLE_MEMBER"..]) 만료 시간이 5분, 빠른 대신에 수명이 정해둔 대로 사용한다는 단점
            rq.setCrossDomainCookie("refreshToken", refreshToken);
            // refresh token 유효 기간 길다 엑세스 토큰이 만료되어 갱신될 때만 사용한다.
            // 통신과정에서 탈취당할 위험이 큰 엑세스 토큰 의 만료기간을 짧게 하고
            // 리프레시 토큰으로 주기적으로 재발급함으로써 피해를 최소화한다.
            rq.removeCookie("redirectUrlAfterSocialLogin"); // MemberController에서 만든 URL

            response.sendRedirect(redirectUrlAfterSocialLogin); // 내가 있던 브라우저로 redirect , 쿠키 2개가 (엑세스,리프레시)생김
            return; // 필수 , 이게 없으면 이후의 디폴트 처리로 넘어간다.
        }

        // 특별한 경우가 아니면 원래하던 로직대로 처리해라!! 타임리프로 소셜로그인 필요할때 필요
        super.onAuthenticationSuccess(request, response, authentication);
    }
}