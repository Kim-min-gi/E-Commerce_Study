package com.study.ecommerce.config;

import com.study.ecommerce.domain.Member;
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


        Optional<Member> findByMember = memberRepository.findByEmail(email);

        if (findByMember.isPresent()){

            return new CustomUserDetails(findByMember.get());
        }

        return null;
    }



}
