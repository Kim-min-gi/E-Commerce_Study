package com.study.ecommerce.service;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.response.MemberResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.IntStream;


@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

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
    @DisplayName("member List 확인")
    void getMember() {


        List<Member> members = IntStream.range(1,31).mapToObj(i ->
                Member.builder()
                        .email("testing" + i + "@gmail.com")
                        .name("사용자이름" + i)
                        .password("!Aa123456")
                        .role("ROLE_USER")
                        .build()
        ).toList();

        memberRepository.saveAll(members);

        Pageable pageable = PageRequest.of(0,10);


        List<MemberResponse> list = adminService.getMembers(pageable);


        Assertions.assertEquals("testing1@gmail.com",list.get(0).getEmail());
        Assertions.assertEquals("사용자이름1",list.get(0).getName());
        Assertions.assertEquals(10,list.size());

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


       MemberResponse findMember = adminService.getMember(member.getId());



        Assertions.assertEquals(member.getId(),findMember.getId());
        Assertions.assertEquals("testing@gmail.com",findMember.getEmail());
        Assertions.assertEquals("name",findMember.getName());

    }


}