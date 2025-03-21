package com.study.ecommerce.controller;

import com.study.ecommerce.exception.AdminCodeNotMatch;
import com.study.ecommerce.request.MemberRequest;
import com.study.ecommerce.request.MemberSignUp;
import com.study.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @Value("${spring.adminCode}")
    private String adminCode;


    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberSignUp memberSignUp){
        authService.signup(memberSignUp);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/resign")
    public ResponseEntity<Void> resign(@RequestBody MemberRequest memberRequest){
        authService.resign(memberRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("/auth/signup/admin")
    public ResponseEntity<Void> adminSignup(@Valid @RequestBody MemberSignUp memberSignUp){

        if (!memberSignUp.getCode().equals(adminCode)){
            throw new AdminCodeNotMatch();
        }

        authService.adminSignup(memberSignUp);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
