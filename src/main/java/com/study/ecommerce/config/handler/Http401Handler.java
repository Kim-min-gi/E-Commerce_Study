package com.study.ecommerce.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.SecurityConfig;
import com.study.ecommerce.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class Http401Handler implements AuthenticationEntryPoint { //인증예외

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
        String acceptHeader = request.getHeader("Accept");
        log.info("요청 Accept 헤더: {}", acceptHeader);

        // HTML 요청인 경우: 로그인 페이지로 리다이렉트
        if ((acceptHeader == null || acceptHeader.toLowerCase().contains("text/html"))
                && !SecurityConfig.PERMIT_ALL_URIS.contains(uri)) {
            response.sendRedirect("/loginPage");
            return;
        }


        log.info("[인증오류] 로그인이 필요합니다.");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("401")
                .message("로그인이 필요합니다.")
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(),errorResponse);
    }
}
