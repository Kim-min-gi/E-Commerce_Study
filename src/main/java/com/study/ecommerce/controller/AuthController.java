package com.study.ecommerce.controller;

import com.study.ecommerce.request.MemberSignUp;
import com.study.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    @PostMapping("/auth/signup")
    public long signup(@RequestBody MemberSignUp memberSignUp){
        return authService.signup(memberSignUp);
    }


    @GetMapping("/auth/login")
    public String login(){
        return "로그인페이지입니다.";
    }


    @PostMapping("/auth/resign")
    public void resign(@RequestBody long id){
        authService.resign(id);
    }




}
