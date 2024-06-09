package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;


    public long signup(MemberSignUp memberSignUp){

        //중복체크


        //security passwordEncoder 설정

        Member member = memberRepository.save(memberSignUp.toEntity());

        return member.getId();

    }



    public void resign(long id){

        Optional<Member> member = memberRepository.findById(id);

        member.ifPresent(memberRepository::delete);

    }




}
