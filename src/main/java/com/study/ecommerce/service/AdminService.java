package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;


    public List<MemberResponse> getMembers(Pageable pageable){
            return memberRepository.findAll(pageable).stream().map(MemberResponse::from).toList();
    }


    public MemberResponse getMember(long id){
        Optional<Member> findMember = memberRepository.findById(id);

        return MemberResponse.builder()
                .id(findMember.get().getId())
                .email(findMember.get().getEmail())
                .name(findMember.get().getName())
                .build();
    }




}
