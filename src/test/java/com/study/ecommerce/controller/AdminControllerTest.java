package com.study.ecommerce.controller;


import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;



    @BeforeEach
    void clean(){
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("member List 출력")
    @CustomMockMember
    void test1() throws Exception {
        List<Member> members = IntStream.range(1,31).mapToObj(i ->
                Member.builder()
                        .email("testing" + i + "@gmail.com")
                        .name("사용자이름" + i)
                        .password("!Aa123456")
                        .role("ROLE_USER")
                        .build()
        ).toList();

        memberRepository.saveAll(members);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/member?page=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("testing1@gmail.com"))
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("member 한명 조회")
    @CustomMockMember
    void test2() throws Exception{

        Member member = Member.builder()
                .email("testing@gmail.com")
                .name("name")
                .password("1234")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/member/{id}",member.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("testing@gmail.com"))
                .andDo(MockMvcResultHandlers.print());

    }






}