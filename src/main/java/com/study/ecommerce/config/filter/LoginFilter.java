package com.study.ecommerce.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.jwt.JwtUtil;
import com.study.ecommerce.domain.token.RefreshToken;
import com.study.ecommerce.repository.token.RefreshTokenRepository;
import com.study.ecommerce.response.LoginResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(ObjectMapper objectMapper, JwtUtil jwtUtil, String url, RefreshTokenRepository refreshTokenRepository){
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.setFilterProcessesUrl(url);
        this.refreshTokenRepository = refreshTokenRepository;
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



    @Override   // 로그인 성공시
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


        String email = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();

        String access = jwtUtil.createToken("Authorization",email,role,600000L);
        String refresh = jwtUtil.createToken("refresh",email,role,86400000L);

        //Response 생성
        LoginResponse.AuthenticatedResponseDto authenticatedResponseDto = LoginResponse.AuthenticatedResponseDto.builder()
                .grantType("Bearer")
                .accessToken(access)
                .refreshToken(refresh)
                .build();

        LoginResponse.MemberInfoResponseDto memberInfoResponseDto = LoginResponse.MemberInfoResponseDto.builder()
                .email(email)
                .role(role)
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .authenticatedResponseDto(authenticatedResponseDto)
                .memberInfoResponseDto(memberInfoResponseDto)
                .build();


        response.setHeader("Authorization","Bearer " + access);
        //response.setHeader("refresh",refresh);
        response.addCookie(createCookie("refresh",refresh));

        //redis 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refresh)
                .email(email)
                .build();

        refreshTokenRepository.save(refreshToken);

        //Response json 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));
        response.setStatus(HttpStatus.OK.value());


    }

    @Override  // 로그인 실패시
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }


    @Getter
    private static class EmailPassword{
        private String email;
        private String password;
    }

    private Cookie createCookie(String key, String value){


        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);


        //cookie.setSecure(true); //https 용
        //cookie.setPath("/"); //쿠키가 적용될 범위

        return cookie;
    }




}
