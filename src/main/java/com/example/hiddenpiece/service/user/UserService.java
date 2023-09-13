package com.example.hiddenpiece.service.user;

import com.example.hiddenpiece.auth.JwtUtil;
import com.example.hiddenpiece.auth.TokenDto;
import com.example.hiddenpiece.domain.dto.user.*;
import com.example.hiddenpiece.domain.entity.user.User;
import com.example.hiddenpiece.domain.repository.follow.FollowRepository;
import com.example.hiddenpiece.domain.repository.user.UserRepository;
import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.redis.RedisService;
import com.example.hiddenpiece.security.CookieManager;
import com.example.hiddenpiece.security.CustomUserDetails;
import com.example.hiddenpiece.service.follow.FollowService;
import com.example.hiddenpiece.service.image.UserImageHandler;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

import static com.example.hiddenpiece.exception.CustomExceptionCode.*;
import static com.example.hiddenpiece.security.CookieManager.ACCESS_TOKEN;
import static com.example.hiddenpiece.security.CookieManager.REFRESH_TOKEN;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final CookieManager cookieManager;
    private final FollowService followService;
    private final FollowRepository followRepository;
    private final UserImageHandler userImageHandler;

    public static final String DEFAULT_PROFILE_IMG_PATH = "/static/img/profile_default.jpg";

    // 회원가입
    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new CustomException(ALREADY_EXIST_USER);
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ALREADY_EXIST_EMAIL);
        }

        User user = requestDto.toEntity(passwordEncoder);
        return new SignupResponseDto(userRepository.save(user));
    }

    // 로그아웃
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 access token 불러오기
        String accessToken = cookieManager.getCookie(request, ACCESS_TOKEN);

        // redis 에서 토큰 찾기 위해 유저 정보 탐색
        Claims claims = jwtUtil.parseClaims(accessToken);
        if (claims == null) {
            throw new CustomException(EXPIRED_JWT);
        }

        String username = claims.getSubject();
        String redisRefreshToken = redisService.getValues(username);

        // 토큰 관련 쿠키 제거
        cookieManager.deleteCookie(response, ACCESS_TOKEN);
        cookieManager.deleteCookie(response, REFRESH_TOKEN);

        // redis 에 있는 refresh token 지우고 access token 블랙리스트 등록
        if (redisService.checkExistsValue(redisRefreshToken)) {
            redisService.deleteValuesByKey(username);

            long accessTokenExpirationMillis = jwtUtil.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
        }
    }

    // 토큰 재발급
    @Transactional
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("토큰 재발급 시작");
        String refreshToken = cookieManager.getCookie(request, REFRESH_TOKEN);

        try {
            Claims claims = jwtUtil.parseClaims(refreshToken);
            String username = claims.getSubject();
            String redisRefreshToken = redisService.getValues(username);

            if (redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
                User user = findUserByUsername(username);
                CustomUserDetails userDetails = CustomUserDetails.of(user);

                TokenDto tokenDto = jwtUtil.generateTokenDto(userDetails);
                String newAccessToken = tokenDto.getAccessToken();

                long accessTokenExpirationMillis = jwtUtil.getAccessTokenExpirationMillis() / 1000;

                cookieManager.setCookie(response, newAccessToken, ACCESS_TOKEN, accessTokenExpirationMillis);
                log.info("재발급 성공");
            } else {
                throw new CustomException(REISSUE_FAILED);
            }
        } catch (Exception e) {
            log.error("재발급 실패: 리프레쉬 토큰 만료");
            cookieManager.deleteCookie(response, ACCESS_TOKEN);
            cookieManager.deleteCookie(response, REFRESH_TOKEN);
            throw new CustomException(REISSUE_FAILED);
        }
    }

    // 유저 프로필 조회
    public UserProfileResponseDto readUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        return UserProfileResponseDto.builder()
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .numberOfWrittenArticle(0)     // TODO 기능 구현시 구현 예정
                .numberOfWrittenComment(0)     // TODO 기능 구현시 구현 예정
                .followingCount(followService.getCountOfFollowing(user))
                .followerCount(followService.getCountOfFollower(user))
                .build();
    }

    // 나의 프로필 조회
    public UserProfileResponseDto readMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        return UserProfileResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .numberOfWrittenArticle(0)     // TODO 기능 구현시 구현 예정
                .numberOfWrittenComment(0)     // TODO 기능 구현시 구현 예정
                .followingCount(followService.getCountOfFollowing(user))
                .followerCount(followService.getCountOfFollower(user))
                .build();
    }

    public UserProfileResponseDto readMiniProfile(HttpServletRequest req) {
        String accessToken = cookieManager.getCookie(req, ACCESS_TOKEN);
        if (accessToken != null) {
            String username = jwtUtil.getAuthentication(accessToken).getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

            return UserProfileResponseDto.builder()
                    .username(user.getUsername())
                    .profileImg(user.getProfileImg())
                    .build();
        }

        return null;
    }

    // 유저의 이름, 등록한 이메일 기반으로 아이디 찾기
    @Transactional
    public String findUsername(RequestFindUsernameDto dto) {
        // 실명과 이메일로 사용자 존재 여부 확인
        if (!userRepository.existsByRealNameAndEmail(dto.getRealName(), dto.getEmail())) {
            throw new CustomException(NOT_FOUND_USER);
        }

        // 실명, 이메일, 그리고 질문에 따른 답변이 일치하는지 확인
        if (!userRepository.existsByRealNameAndEmailAndQuestionAndAnswer(dto.getRealName(), dto.getEmail(), dto.getQuestion(), dto.getAnswer())) {
            throw new CustomException(WRONG_SECURITY_ANSWER);
        }

        return userRepository.findUsernameByRealNameAndEmailAndQuestionAndAnswer(dto.getRealName(), dto.getEmail(), dto.getQuestion(), dto.getAnswer());
    }

    // 유저의 이름, 아이디로 계정 확인
    @Transactional
    public Long findAccount(RequestFindPasswordDto dto) {
        if (!userRepository.existsByUsernameAndRealName(dto.getUsername(), dto.getRealName())) {
            throw new CustomException(NOT_FOUND_USER);
        }

        return userRepository.existUserByRealNameAndUsername(dto.getRealName(), dto.getUsername());
    }

    // 유저 비밀번호 수정
    @Transactional
    public void updatePassword(RequestChangePasswordDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new CustomException(ALREADY_USED_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    // 유저 정보 수정
    @Transactional
    public void updateUserInfo(RequestUpdateUserInfoDto dto, MultipartFile image, Long userId, String username) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (!user.getUsername().equals(username)) throw new CustomException(USER_NOT_MATCH);

        String path = userImageHandler.parseFileInfo(userId, image);
        if (path == null) path = user.getProfileImg();

        user.updateInfo(passwordEncoder.encode(dto.getPassword()), dto.getEmail(), dto.getPhone(), path);
        userRepository.save(user);
        log.info("이미지 등록 성공");
    }

    // 계정 탈퇴
    @Transactional
    public void deleteUser(RequestDeleteUserDto dto, String username, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (!user.getUsername().equals(username)) throw new CustomException(USER_NOT_MATCH);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        userRepository.delete(user);
    }

    public User findUserAndCheckUserExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }

    public Integer countUsers() {
        return (int) userRepository.count();
    }

    // 내가 팔로우 하고 있는 유저 목록 페이징 조회
    public Page<ResponseFollowingDto> readMyFollowings(Integer num, Integer limit, String username, Long userId) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (!currentUser.getUsername().equals(username)) throw new CustomException(USER_NOT_MATCH);

        Pageable pageable = PageRequest.of(num, limit);

        return followRepository.findFollowByFromUser(currentUser, pageable);
    }

    // 나를 팔로우 하고 있는 유저 목록 페이징 조회
    public Page<ResponseFollowerDto> readMyFollowers(Integer num, Integer limit, String username, Long userId) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (!currentUser.getUsername().equals(username)) throw new CustomException(USER_NOT_MATCH);

        Pageable pageable = PageRequest.of(num, limit);

        return followRepository.findFollowByToUser(currentUser, pageable);
    }
}
