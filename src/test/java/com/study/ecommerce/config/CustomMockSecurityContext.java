package com.study.ecommerce.config;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;


@RequiredArgsConstructor
public class CustomMockSecurityContext implements WithSecurityContextFactory<CustomMockMember> {

    private final MemberRepository memberRepository;

    @Override
    public SecurityContext createSecurityContext(CustomMockMember annotation) {
        String email = annotation.email();

        Member member = Member.builder()
                .email(email)
                .password(annotation.password())
                .role(annotation.role())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authToken);

        return context;

    }
}
