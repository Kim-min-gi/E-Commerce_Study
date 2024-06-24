package com.study.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;



//    @BeforeEach
//    void clean(){
//        memberRepository.deleteAll();
//    }


//    @Test
//    @DisplayName("/member 호출 시 member List 출력")
//    @CustomMockMember
//    void member() throws Exception {
//        Member member = Member.builder()
//                .email("testing@gmail.com")
//                .name("name")
//                .password("1234")
//                .role("ROLE_ADMIN")
//                .build();
//
//        memberRepository.save(member);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/member")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("testing@gmail.com"))
//                .andDo(MockMvcResultHandlers.print());
//
//
//    }



}