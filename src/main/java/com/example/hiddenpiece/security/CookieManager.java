package com.example.hiddenpiece.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";

    public void setCookie(HttpServletResponse res, String value, String name, long expiration){
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(expiration)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        res.addHeader("Set-Cookie", cookie.toString());
    }

    public String getCookie(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void deleteCookie(HttpServletResponse res, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .maxAge(0)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        res.addHeader("Set-Cookie", cookie.toString());
    }
}
