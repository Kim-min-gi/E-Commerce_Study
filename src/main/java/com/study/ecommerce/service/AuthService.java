package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.exception.AlreadyExistsEmailException;
import com.study.ecommerce.exception.NotFoundMemberException;
import com.study.ecommerce.exception.ResignUnauthorizedException;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberRequest;
import com.study.ecommerce.request.MemberSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void resign(MemberRequest memberRequest){

        Member member = memberRepository.findById(memberRequest.getId()).orElseThrow(NotFoundMemberException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean matches = passwordEncoder.matches(memberRequest.getPassword(), member.getPassword());


        if (member.getEmail().equals(email) && matches){
            memberRepository.delete(member);
        }else{
            throw new ResignUnauthorizedException();
        }


        //멤버 삭제시 Order 및 리뷰 삭제 추가

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
