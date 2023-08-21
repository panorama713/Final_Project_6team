package com.example.hiddenpiece.oauth;

import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

import static com.example.hiddenpiece.oauth.OAuth2Provider.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Attributes {
    public static final String NAME_ATTRIBUTE_KEY = "email";

    private String id;
    private String name;
    private String email;
    private String provider;
    private String profileImg;

    @Builder
    public OAuth2Attributes(String id, String name, String email, String provider, String profileImg) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.profileImg = profileImg;
    }

    public static OAuth2Attributes of(String provider, OAuth2User oAuth2User) {
        if (provider.equals(NAVER.getProvider())) {
            return ofNaver(oAuth2User);
        }

        if (provider.equals(KAKAO.getProvider())) {
            return ofKakao(oAuth2User);
        }

        if (provider.equals(GOOGLE.getProvider())) {
            return ofGoogle(oAuth2User);
        }

        return null;
    }

    private static OAuth2Attributes ofNaver(OAuth2User oAuth2User) {
        Map<String, Object> response = oAuth2User.getAttribute("response");

        return new OAuth2Attributes(
                (String) response.get("id"),
                (String) response.get("name"),
                (String) response.get("email"),
                NAVER.getProvider(),
                (String) response.get("profile_image"));
    }

    private static OAuth2Attributes ofKakao(OAuth2User oAuth2User) {
        Map<String, Object> properties = oAuth2User.getAttribute("properties");
        Map<String, Object> account = oAuth2User.getAttribute("kakao_account");
        return new OAuth2Attributes(
                oAuth2User.getAttribute("id").toString(),
                (String) properties.get("nickname"),
                (String) account.get("email"),
                KAKAO.getProvider(),
                (String) properties.get("profile_image")
        );
    }

    private static OAuth2Attributes ofGoogle(OAuth2User oAuth2User) {
        return new OAuth2Attributes(
                oAuth2User.getAttribute("sub"),
                oAuth2User.getAttribute("name"),
                oAuth2User.getAttribute("email"),
                GOOGLE.getProvider(),
                oAuth2User.getAttribute("picture")
        );
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> changedAttributes = new HashMap<>();
        changedAttributes.put("id", this.id);
        changedAttributes.put("name", this.name);
        changedAttributes.put("email", this.email);
        changedAttributes.put("provider", this.provider);
        changedAttributes.put("profile_img", this.profileImg);

        return changedAttributes;
    }
}