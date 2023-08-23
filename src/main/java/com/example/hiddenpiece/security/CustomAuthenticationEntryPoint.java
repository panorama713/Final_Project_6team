package com.example.hiddenpiece.security;

import com.example.hiddenpiece.exception.CustomExceptionCode;
import com.example.hiddenpiece.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");

        if (exception instanceof CustomExceptionCode errorCode) {
            setResponse(response, errorCode);
            return;
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    // 응답을 한글로 출력
    private void setResponse(HttpServletResponse response, CustomExceptionCode errorCode) throws IOException {
        log.error("{}: {}", errorCode.getHttpStatus(), errorCode.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = new ErrorResponse(errorCode.getHttpStatus().name(), errorCode.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().print(objectMapper.writeValueAsString(errorResponse));
    }
}
