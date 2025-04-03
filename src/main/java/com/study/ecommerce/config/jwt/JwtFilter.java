package com.study.ecommerce.config.jwt;

import com.study.ecommerce.config.CustomUserDetails;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.token.BlackListAccessToken;
import com.study.ecommerce.repository.token.BlackListTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final BlackListTokenRepository blackListTokenRepository;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.split(" ")[1];

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("Authorization")){

            PrintWriter writer = response.getWriter();
            writer.print("invalid Authorization token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

        Boolean expired = jwtUtil.isExpired(accessToken);// 토큰 유효기간 확인
        Optional<BlackListAccessToken> byId = blackListTokenRepository.findById(email); //블랙리스트 조회

        if (byId.isPresent() || expired){
            PrintWriter writer = response.getWriter();
            writer.print("Authorization token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Member member = Member.builder()
                .email(email)
                .password("temppassword")
                .role(role)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);


        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

//        String authorization = request.getHeader("Authorization");
//
//
//        //Authorization 헤더 검증
//        if (authorization == null || !authorization.startsWith("Bearer ")){
//
//            filterChain.doFilter(request,response);
//
//            return;
//        }
//
//        String token = authorization.split(" ")[1];
//
//        //토큰 소멸 시간 검증
//        if (jwtUtil.isExpired(token)){
//
//            filterChain.doFilter(request,response);
//
//            return;
//        }
//
////        Long id = jwtUtil.getId(token);
//        String email = jwtUtil.getEmail(token);
//        String role = jwtUtil.getRole(token);
//
//        Member member = Member.builder()
//                .email(email)
//                .password("temppassword")
//                .role(role)
//                .build();
//
////        member.setId(id);
//
//
//        //UserDetails에 회원 정보 객체 담기
//        CustomUserDetails customUserDetails = new CustomUserDetails(member);
//
//        //스프링 시큐리티 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
//
//        //세션에 사용자 등록
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//
//        filterChain.doFilter(request, response);


    }

}
