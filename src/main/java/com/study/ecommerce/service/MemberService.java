package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;


    public List<MemberResponse> getMember(){
            return memberRepository.findAll().stream().map(MemberResponse::new).collect(Collectors.toList());
    }



}
