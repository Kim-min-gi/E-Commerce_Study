package com.study.ecommerce.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class Http403Handler implements AccessDeniedHandler { //인증오류

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("[인증오류] 403");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("403")
                .message("접근할 수 없습니다.")
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        objectMapper.writeValue(response.getWriter(),errorResponse);
    }
}
