package com.study.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.request.MemberRequest;
import com.study.ecommerce.request.MemberSignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.ecommerce.com", uriPort = 433)
@ExtendWith(RestDocumentationExtension.class)
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

        mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/signup")
                    .content(objectMapper.writeValueAsString(member))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("auth/member-signup",
                        requestFields(
                                fieldWithPath("email").description("회원 아이디"),
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("code").description("ADMIN 가입 확인코드")
                        )))
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

        mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/signup/admin")
                        .content(objectMapper.writeValueAsString(member))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("auth/admin-signup",
                        requestFields(
                                fieldWithPath("email").description("관리자 아이디"),
                                fieldWithPath("name").description("관리자 이름"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("code").description("ADMIN 가입 확인코드")
                        )))
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

        MemberRequest memberRequest = MemberRequest.builder()
                .id(memberId)
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/resign")
//                        .content(objectMapper.writeValueAsString(memberId))
                        .content(objectMapper.writeValueAsString(memberRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("auth/member-resign",
                        requestFields(
                                fieldWithPath("id").description("회원 번호")
                        )))
                .andDo(MockMvcResultHandlers.print());

    }
}