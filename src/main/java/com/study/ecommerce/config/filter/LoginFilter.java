package com.study.ecommerce.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.CustomUserDetails;
import com.study.ecommerce.config.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public LoginFilter(ObjectMapper objectMapper, JwtUtil jwtUtil,String url){
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.setFilterProcessesUrl(url);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        EmailPassword emailPassword = null;

        try {

            emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                emailPassword.email,
                emailPassword.password
        );

        token.setDetails(this.authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(token);
    }

    @Getter
    private static class EmailPassword{
        private String email;
        private String password;
    }

    @Override   // 로그인 성공시
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String email = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();

        String role = authority.getAuthority();

        // JWT 토큰 생성
        String token = jwtUtil.createToken(email,role);

        // 응답 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + token);


    }

    @Override  // 로그인 실패시
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }






}
