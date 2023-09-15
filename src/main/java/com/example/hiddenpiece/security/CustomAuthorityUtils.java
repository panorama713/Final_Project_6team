package com.example.hiddenpiece.security;

import com.example.hiddenpiece.domain.entity.user.Role;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class CustomAuthorityUtils {
    public static List<GrantedAuthority> createAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }

    public static void verifiedRole(Role role) {
        if (role.toString() == null) {
            throw new CustomException(CustomExceptionCode.USER_ROLE_NOT_EXIST);
        } else if (!role.toString().equals(Role.USER.toString()) && !role.toString().equals(Role.ADMIN.toString())) {
            throw new CustomException(CustomExceptionCode.USER_ROLE_INVALID);
        }
    }
}
