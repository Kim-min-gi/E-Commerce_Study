package com.study.ecommerce.controller;


import com.study.ecommerce.domain.Member;
import com.study.ecommerce.response.MemberResponse;
import com.study.ecommerce.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final MemberService memberService;

    @GetMapping("/member")
    public List<MemberResponse> member() throws Exception{
        return memberService.getMember();
    }



}
