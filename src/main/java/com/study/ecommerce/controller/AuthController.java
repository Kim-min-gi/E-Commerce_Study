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


    @PostMapping("/signup")
    void signup(@RequestBody MemberSignUp memberSignUp){
        authService.signup(memberSignUp);
    }

    @GetMapping("/login")
    void login(){

    }

    @GetMapping("/resign")
    void resign(){

    }




}
