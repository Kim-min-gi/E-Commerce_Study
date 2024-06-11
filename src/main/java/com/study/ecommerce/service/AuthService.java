package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.exception.AlreadyExistsEmailException;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public long signup(MemberSignUp memberSignUp){


        Optional<Member> findMember = memberRepository.findByEmail(memberSignUp.getEmail());

        //중복체크
        if (findMember.isPresent()){
            throw new AlreadyExistsEmailException();
        }


        String encryptedPassword = passwordEncoder.encode(memberSignUp.getPassword());


        MemberSignUp saveMember = MemberSignUp.builder()
                .name(memberSignUp.getName())
                .email(memberSignUp.getEmail())
                .password(encryptedPassword)
                .build();


        Member member = memberRepository.save(saveMember.toEntity());

        return member.getId();

    }



    public void resign(long id){

        Optional<Member> member = memberRepository.findById(id);

        member.ifPresent(memberRepository::delete);

    }




}
