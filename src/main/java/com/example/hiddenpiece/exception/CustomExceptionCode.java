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
    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    /*
     * 409
     */
    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USER_NOT_MATCH(HttpStatus.CONFLICT, "해당 사용자가 존재하지 않거나, 아이디 혹은 비밀번호가 일치하지 않습니다."),
    REISSUE_FAILED(HttpStatus.CONFLICT, "다시 로그인하여 주시기 바랍니다."),
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
