package com.study.ecommerce.controller;


import com.study.ecommerce.config.CustomMockMember;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.util.List;
import java.util.stream.IntStream;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.ecommerce.com", uriPort = 433)
@ExtendWith(RestDocumentationExtension.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

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

        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/member?page=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("testing1@gmail.com"))
                .andDo(document("admin/admin-list",
                        responseFields(
                                fieldWithPath("[]").description("회원 리스트 배열"),
                                fieldWithPath("[].id").description("회원 번호"),
                                fieldWithPath("[].email").description("회원 아이디"),
                                fieldWithPath("[].name").description("회원 이름")
                        )
                        ))
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

        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/member/{id}",member.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("testing@gmail.com"))
                .andDo(document("admin/admin-inquiry",
                        pathParameters(
                            parameterWithName("id").description("회원번호")
                        ),
                        responseFields(
                                        fieldWithPath("id").description("회원 번호"),
                                        fieldWithPath("email").description("회원 아이디"),
                                        PayloadDocumentation.fieldWithPath("name").description("회원 이름")
                                )
                ))
                .andDo(MockMvcResultHandlers.print());

    }






}