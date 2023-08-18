package com.example.hiddenpiece.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttribute = "";

        Map<String, Object> attributes = new HashMap<>();

        // Naver 로직
        if (registrationId.equals("naver")) {
            attributes.put("provider", "naver");

            // 받은 사용자 데이터를 정리한다.
            Map<String, Object> responseMap = oAuth2User.getAttribute("response");
            attributes.put("id", responseMap.get("sub"));
            attributes.put("email", responseMap.get("email"));
            attributes.put("name", responseMap.get("name"));
            nameAttribute = "email";
        }

        // Kakao 로직
        if (registrationId.equals("kakao")) {
            attributes.put("provider", "kakao");
            attributes.put("id", oAuth2User.getAttribute("id"));

            Map<String, Object> propertiesMap = oAuth2User.getAttribute("properties");
            attributes.put("name", propertiesMap.get("nickname"));
            attributes.put("profile_img", propertiesMap.get("profile_image"));

            Map<String, Object> accountMap = oAuth2User.getAttribute("kakao_account");
            attributes.put("email", accountMap.get("email"));

            nameAttribute = "email";
        }

        // Google 로직
        if (registrationId.equals("google")) {
            attributes.put("provider", "google");
            attributes.put("id", oAuth2User.getAttribute("sub"));
            attributes.put("name", oAuth2User.getAttribute("name"));
            attributes.put("email", oAuth2User.getAttribute("email"));
            attributes.put("profile_img", oAuth2User.getAttribute("picture"));

            nameAttribute = "email";
        }

        log.info(attributes.toString());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes,
                nameAttribute
        );
    }
}
