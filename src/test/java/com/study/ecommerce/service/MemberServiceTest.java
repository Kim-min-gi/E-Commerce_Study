package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.response.MemberResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

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

        int count = memberRepository.findAll().stream().map(MemberResponse::new).toList().size();

        List<MemberResponse> list = memberRepository.findAll().stream().map(MemberResponse::new).toList();



        Assertions.assertEquals(1,count);
        Assertions.assertEquals("testing@gmail.com",list.get(0).getEmail());
        Assertions.assertEquals("name",list.get(0).getName());
        Assertions.assertEquals(1L,list.get(0).getId());


    }
}