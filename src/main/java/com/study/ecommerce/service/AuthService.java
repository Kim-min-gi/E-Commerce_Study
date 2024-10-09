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
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public void signup(MemberSignUp memberSignUp){

        Member saveMember = duplicateCheckAndSignup(memberSignUp,"ROLE_USER");

        memberRepository.save(saveMember);

    }


    public void adminSignup(MemberSignUp memberSignUp){

        Member saveMember = duplicateCheckAndSignup(memberSignUp,"ROLE_ADMIN");

        memberRepository.save(saveMember);

    }

    @Transactional
    public void resign(long id){

        Optional<Member> member = memberRepository.findById(id);

        member.ifPresent(memberRepository::delete);

    }


    public Member duplicateCheckAndSignup(MemberSignUp memberSignUp,String role){
        Optional<Member> findMember = memberRepository.findByEmail(memberSignUp.getEmail());

        //중복체크
        if (findMember.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encode(memberSignUp.getPassword());


        return Member.form(memberSignUp,encryptedPassword,role);
    }



}
