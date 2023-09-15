package com.example.hiddenpiece.security;

import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsManagerImpl implements UserDetailsManager {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));
        return CustomUserDetails.of(user);
    }

    @Override
    @Transactional
    public void createUser(UserDetails user) {
        // 사용자가 (이미) 있으면 생성할수 없다.
        if (this.userExists(user.getUsername()))
            throw new CustomException(CustomExceptionCode.ALREADY_EXIST_USER);

        userRepository.save(((CustomUserDetails) user).newEntity());
    }

    @Override
    public void updateUser(UserDetails user) {
    }

    @Override
    public void deleteUser(String username) {
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    public boolean userExists(String username) {
        return this.userRepository.existsByUsername(username);
    }
}
