package com.example.hiddenpiece.security;

import com.example.hiddenpiece.domain.entity.user.Role;
import com.example.hiddenpiece.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails extends User implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Role role;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public CustomUserDetails(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public CustomUserDetails(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static CustomUserDetails of(User user) {
        return new CustomUserDetails(user);
    }

    public static CustomUserDetails of(String username, Role role) {
        return new CustomUserDetails(username, role);
    }

    public static CustomUserDetails of(String username, String password, Role role) {
        return new CustomUserDetails(username, password, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return CustomAuthorityUtils.createAuthorities(role);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
