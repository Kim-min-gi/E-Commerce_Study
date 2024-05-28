package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("member List 확인")
    void getMember() {
        Member member = Member.builder()
                .email("testing@gmail.com")
                .name("name")
                .password("1234")
                .build();

        memberRepository.save(member);

        int count = memberRepository.findAll().size();

        Assertions.assertEquals(1,count);

    }
}