package com.example.hiddenpiece.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionCode {
    /*
     * 400
     */
    NOT_MATCH_WRITER(HttpStatus.BAD_REQUEST, "작성자가 아닙니다."),
    ALREADY_LOGOUT_USER(HttpStatus.BAD_REQUEST, "다시 로그인해 주시기 바랍니다."),
    NOT_ALLOW_MORE_REPLY(HttpStatus.BAD_REQUEST, "더 이상 대댓글을 등록할 수 없습니다."),
    IMAGE_COUNT_MISMATCH(HttpStatus.BAD_REQUEST, "전달된 이미지의 수와 업로드된 이미지의 수가 일치하지 않습니다."),
    WRONG_SECURITY_ANSWER(HttpStatus.BAD_REQUEST, "보안 질문 혹은 답변을 다시 확인해 주시기 바랍니다."),

    /*
     * 401
     */
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 다시 로그인 해주시기 바랍니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다. 다시 로그인 해주시기 바랍니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다. 다시 로그인 해주시기 바랍니다."),
    ILLEGAL_ARGUMENT_JWT(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다. 다시 로그인 해주시기 바랍니다."),
    /*
     * 403
     */
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "잘못된 접근입니다."),
    CANNOT_LIKE_YOUR_ARTICLE(HttpStatus.FORBIDDEN, "자신의 글은 좋아요를 누를 수 없습니다."),
    CANNOT_BOOKMARK_YOUR_ARTICLE(HttpStatus.FORBIDDEN, "자신의 글은 북마크할 수 없습니다."),
    CANNOT_FOLLOW_YOURSELF(HttpStatus.FORBIDDEN,"자기 자신은 팔로우 할 수 없습니다." ),
    /*
     * 404
     */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    USER_ROLE_NOT_EXIST(HttpStatus.NOT_FOUND, "권한이 존재하지 않습니다."),
    USER_ROLE_INVALID(HttpStatus.NOT_FOUND, "옳은 권한이 아닙니다."),
    REFRESH_TOKEN_NOT_EXISTS(HttpStatus.NOT_FOUND, "다시 로그인 해주시기 바랍니다."),
    NOT_FOUND_ROADMAP(HttpStatus.NOT_FOUND, "해당 로드맵을 찾을 수 없습니다."),
    NOT_FOUND_CONDITION_ROADMAP(HttpStatus.NOT_FOUND, "해당 조건의 로드맵을 찾을 수 없습니다."),
    NOT_FOUND_ROADMAP_ELEMENT(HttpStatus.NOT_FOUND, "해당 로드맵 요소를 찾을 수 없습니다."),
    ALREADY_DELETED_ELEMENT(HttpStatus.NOT_FOUND, "이미 삭제된 로드맵 요소입니다."),
    NOT_FOUND_ROADMAP_CATEGORY(HttpStatus.NOT_FOUND, "해당 로드맵 카테고리를 찾을 수 없습니다."),
    NOT_FOUND_ROADMAP_BOOKMARK(HttpStatus.NOT_FOUND, "해당 로드맵 북마크를 찾을 수 없습니다."),
    NOT_FOUND_ROADMAP_TODO(HttpStatus.NOT_FOUND, "해당 로드맵 Todo를 찾을 수 없습니다."),
    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    NOT_FOUND_ARTICLE_BOOKMARK(HttpStatus.NOT_FOUND, "북마크한 해당 게시글을 찾을 수 없습니다."),
    NOT_FOUND_LIKE(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "해당 이미지를 찾을 수 없습니다."),
    NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "아직 팔로우 한 인원이 없습니다."),
    /*
     * 409
     */
    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    ALREADY_USED_PASSWORD(HttpStatus.CONFLICT, "기존 비밀번호로 변경할 수 없습니다."),
    USER_NOT_MATCH(HttpStatus.CONFLICT, "해당 사용자가 존재하지 않거나, 아이디 혹은 비밀번호가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.CONFLICT, "비밀번호가 일치하지 않습니다."),
    REISSUE_FAILED(HttpStatus.CONFLICT, "다시 로그인하여 주시기 바랍니다."),
    FOLLOW_FAILED(HttpStatus.CONFLICT, "이미 팔로우 한 유저입니다."),
    UNFOLLOW_FAILED(HttpStatus.CONFLICT, "이미 언팔로우 한 유저입니다."),
    LIKE_FAILED(HttpStatus.CONFLICT, "이미 좋아요 한 게시글입니다."),
    UNLIKE_FAILED(HttpStatus.CONFLICT, "이미 좋아요를 취소한 게시글입니다."),
    CANNOT_BOOKMARK_YOUR_ROADMAP(HttpStatus.CONFLICT, "자신의 로드맵은 북마크할 수 없습니다."),
    ALREADY_EXIST_ARTICLE_BOOKMARK(HttpStatus.CONFLICT, "이미 북마크에 저장된 게시글입니다."),
    ALREADY_EXIST_ROADMAP_BOOKMARK(HttpStatus.CONFLICT, "이미 북마크에 저장된 로드맵입니다."),
    /*
     * 415
     */
    UNSUPPORTED_IMAGE_FORMAT(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 이미지 파일 형식입니다."),
    /*
     * 500
     */
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 문제가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
