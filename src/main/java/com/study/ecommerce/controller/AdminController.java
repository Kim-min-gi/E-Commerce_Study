package com.study.ecommerce.controller;

import com.study.ecommerce.response.AdminResponse;
import com.study.ecommerce.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService memberService;

    @GetMapping("/member")
    public List<AdminResponse> member() throws Exception{
        return memberService.getMember();
    }

    @GetMapping("/member/{id}")
    public List<AdminResponse> member(@RequestParam long id) throws Exception{
        return memberService.getMember();
    }
}
