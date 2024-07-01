package com.study.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberSignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${spring.adminCode}")
    private String adminCode;


    @BeforeEach
    void clean(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    @WithMockUser
    void signup() throws Exception {
        MemberSignUp member = MemberSignUp.builder()
                .email("Testing@naver.com")
                .name("Testing")
                .password("!Aa123456")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                    .content(objectMapper.writeValueAsString(member))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    @DisplayName("관리자 회원가입")
    @WithMockUser
    void adminSignup() throws Exception {
        MemberSignUp member = MemberSignUp.builder()
                .email("Testing@naver.com")
                .name("Testing")
                .password("!Aa123456")
                .code(adminCode)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup/admin")
                        .content(objectMapper.writeValueAsString(member))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("관리자 회원가입 실패")
    @WithMockUser
    void adminSignupFail() throws Exception {
        MemberSignUp member = MemberSignUp.builder()
                .email("Testing@naver.com")
                .name("Testing")
                .password("!Aa1234")
                .code("adminCode")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup/admin")
                        .content(objectMapper.writeValueAsString(member))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    @DisplayName("회원탈퇴")
    @WithMockUser
    void resign() throws Exception {

        Member member = Member.builder()
                .email("Testing@naver.com")
                .name("Testing")
                .password("1234")
                .role("ROLE_USER")
                .build();

        Member saveMember = memberRepository.save(member);

        long memberId = saveMember.getId();


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/resign")
                        .content(objectMapper.writeValueAsString(memberId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

    }
}