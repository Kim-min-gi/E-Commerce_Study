package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.response.AdminResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@Transactional
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MemberRepository memberRepository;


    @BeforeEach
    void clean(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("member List 확인")
    void getMember() {
        Member member = Member.builder()
                .email("testing@gmail.com")
                .name("name")
                .password("!Aa123456")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        int count = memberRepository.findAll().stream().map(AdminResponse::new).toList().size();

        List<AdminResponse> list = memberRepository.findAll().stream().map(AdminResponse::new).toList();



        Assertions.assertEquals("testing@gmail.com",list.get(0).getEmail());
        Assertions.assertEquals("name",list.get(0).getName());

    }

    @Test
    @DisplayName("member 한명 조회")
    void getMemberId() {
        Member member = Member.builder()
                .email("testing@gmail.com")
                .name("name")
                .password("!Aa123456")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);


       AdminResponse findMember = adminService.getMember(member.getId());



        Assertions.assertEquals(member.getId(),findMember.getId());
        Assertions.assertEquals("testing@gmail.com",findMember.getEmail());
        Assertions.assertEquals("name",findMember.getName());

    }


}