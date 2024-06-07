package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;


    public void signup(MemberSignUp memberSignUp){

        //중복체크


        //security passwordEncoder 설정

        memberRepository.save(memberSignUp.toEntity());
    }




}
