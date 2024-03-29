package com.ll.edubridge.global.security;


import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.domain.point.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final PointService pointService;

    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = oAuth2User.getName();
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        String nickname = "";
        String profileImgUrl = "";
        String username = "";

        switch (providerTypeCode) {
            case "KAKAO":
                Map<String, Object> attributes = oAuth2User.getAttributes();
                Map attributesProperties = (Map) attributes.get("properties");

                nickname = (String) attributesProperties.get("nickname") + oauthId.substring(2,6);
                profileImgUrl = (String) attributesProperties.get("profile_image");
                break;
            case "GOOGLE":
                nickname = (String) oAuth2User.getAttributes().get("name") + oauthId.substring(2,6);
                profileImgUrl = (String) oAuth2User.getAttributes().get("picture");
                break;
        }

        username = providerTypeCode + "__%s".formatted(oauthId);


        Member member = memberService.modifyOrJoin(username, providerTypeCode, nickname, profileImgUrl).getData();

        if(!member.isVisitedToday()) {
            memberService.visit(member);
        }

        return new SecurityUser(member.getId(), member.getUsername(), member.getPassword(), member.getAuthorities());
    }
}