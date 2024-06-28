package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.exception.AlreadyExistsEmailException;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberSignUp;
import com.study.ecommerce.response.AdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;


    public List<AdminResponse> getMembers(){
            return memberRepository.findAll().stream().map(AdminResponse::new).collect(Collectors.toList());
    }


    public AdminResponse getMember(long id){
        Optional<Member> findMember = memberRepository.findById(id);

        return AdminResponse.builder()
                .id(findMember.get().getId())
                .email(findMember.get().getEmail())
                .name(findMember.get().getName())
                .build();
    }




}
