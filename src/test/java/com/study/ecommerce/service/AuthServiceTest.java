package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberSignUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void clean(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void signup() throws Exception{
        MemberSignUp member = MemberSignUp.builder()
                .email("Testing@naver.com")
                .name("Testing")
                .password("1234")
                .build();

        MemberSignUp member2 = MemberSignUp.builder()
                .email("Testing2@naver.com")
                .name("Testing2")
                .password("1234")
                .build();


        memberRepository.save(member.toEntity());
        memberRepository.save(member2.toEntity());

        int count = memberRepository.findAll().size();
        Optional<Member> findMember = memberRepository.findByEmail("Testing@naver.com");


        Assertions.assertEquals(2,count);
        Assertions.assertEquals("Testing@naver.com",findMember.get().getEmail());
        Assertions.assertEquals("Testing",findMember.get().getName());
        Assertions.assertEquals("1234",findMember.get().getPassword());

    }
}