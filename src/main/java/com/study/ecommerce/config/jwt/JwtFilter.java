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

        String token = authorization.split(" ")[1];

        try {
            // 토큰 카테고리 검증
            if (!"Authorization".equals(jwtUtil.getCategory(token))) {
                throw new RuntimeException("Invalid token category");
            }

            String email = jwtUtil.getEmail(token);
            String role = jwtUtil.getRole(token);

            // 토큰 만료 여부 또는 블랙리스트 확인
            if (jwtUtil.isExpired(token) || blackListTokenRepository.findById(email).isPresent()) {
                throw new RuntimeException("Expired or blacklisted token");
            }

            // 인증객체 생성 및 SecurityContext에 설정
            Member member = Member.builder()
                    .email(email)
                    .password("temppassword")
                    .role(role)
                    .build();

            CustomUserDetails userDetails = new CustomUserDetails(member);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            // 리팩터링: 로그 레벨 DEBUG로 변경하고, SecurityContext 명시적으로 클리어
            log.debug("[JwtFilter] 토큰 인증 실패: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);


//        String accessToken = authorization.split(" ")[1];
//        String category = jwtUtil.getCategory(accessToken);
//
//        if (!category.equals("Authorization")){
//
//            PrintWriter writer = response.getWriter();
//            writer.print("invalid Authorization token");
//
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        String email = jwtUtil.getEmail(accessToken);
//        String role = jwtUtil.getRole(accessToken);
//
//        Boolean expired = jwtUtil.isExpired(accessToken);// 토큰 유효기간 확인
//        Optional<BlackListAccessToken> byId = blackListTokenRepository.findById(email); //블랙리스트 조회
//
//        if (byId.isPresent() || expired){
//            PrintWriter writer = response.getWriter();
//            writer.print("Authorization token expired");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        Member member = Member.builder()
//                .email(email)
//                .password("temppassword")
//                .role(role)
//                .build();
//
//        CustomUserDetails customUserDetails = new CustomUserDetails(member);
//
//
//        //스프링 시큐리티 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
//
//        //세션에 사용자 등록
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);



    }

}
