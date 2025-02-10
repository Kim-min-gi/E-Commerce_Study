package com.study.ecommerce.config;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.exception.NotFoundMemberException;
import com.study.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        Member findByMember = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

        return new CustomUserDetails(findByMember);

    }



}
