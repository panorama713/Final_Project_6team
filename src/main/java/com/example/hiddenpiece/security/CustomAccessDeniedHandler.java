package com.example.hiddenpiece.security;

import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");

        if (exception instanceof CustomExceptionCode errorCode) {
            setResponse(response, errorCode);
            return;
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private void setResponse(HttpServletResponse response, CustomExceptionCode errorCode) throws IOException {
        log.error("{}: {}", errorCode.getHttpStatus(), errorCode.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = new ErrorResponse("Access Denied", "접근 제한");

        response.getWriter().print(errorResponse);
    }
}
