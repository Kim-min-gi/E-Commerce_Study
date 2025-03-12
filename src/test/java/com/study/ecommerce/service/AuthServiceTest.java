package com.study.ecommerce.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.exception.AdminCodeNotMatch;
import com.study.ecommerce.exception.NotFoundMemberException;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberRequest;
import com.study.ecommerce.request.MemberSignUp;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }


    void cleanDatabase() {
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


        authService.signup(member);


        int count = memberRepository.findAll().size();
        Optional<Member> findMember = memberRepository.findByEmail("Testing@naver.com");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Assertions.assertEquals(1,count);
        Assertions.assertEquals("Testing@naver.com",findMember.get().getEmail());
        Assertions.assertEquals("Testing",findMember.get().getName());
        Assertions.assertEquals(true,bCryptPasswordEncoder.matches("1234",findMember.get().getPassword()));
    }


    @Test
    @DisplayName("회원탙퇴")
    @Transactional
    void resign() throws Exception{

        //create
        String encode = passwordEncoder.encode("1234");

        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Testing")
                .password(encode)
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Testing@naver.com", null, authorities)
        );

        //delete
        Member member1 = memberRepository.findByEmail("Testing@naver.com").orElseThrow(NotFoundMemberException::new);

        MemberRequest memberRequest = MemberRequest.builder()
                .id(member1.getId())
                .password("1234")
                .build();

        authService.resign(memberRequest);

        Assertions.assertEquals(0,memberRepository.count());

    }


}