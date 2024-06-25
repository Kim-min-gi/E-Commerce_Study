package com.study.ecommerce.controller;

import com.study.ecommerce.exception.AdminCodeNotMatch;
import com.study.ecommerce.request.MemberSignUp;
import com.study.ecommerce.response.AdminResponse;
import com.study.ecommerce.service.AdminService;
import com.study.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService memberService;
    private final AuthService authService;

    @Value("${spring.adminCode}")
    private String adminCode;



    @GetMapping("/admin/member")
    public List<AdminResponse> member() throws Exception{
        return memberService.getMember();
    }


    @PostMapping("/auth/signup/admin")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberSignUp memberSignUp){

        if (!memberSignUp.getCode().equals(adminCode)){
            throw new AdminCodeNotMatch();
        }

        authService.adminSignup(memberSignUp);

        return ResponseEntity.status(200).build();
    }

//    @GetMapping("/admin/member")
//    public String member() throws Exception{
//        return "관리자 페이지입니다.";
//    }

    @GetMapping("/admin/member/{id}")
    public List<AdminResponse> getmembers(@RequestParam long id) throws Exception{
        return memberService.getMember();
    }
}
