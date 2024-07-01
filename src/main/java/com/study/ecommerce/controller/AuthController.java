package com.study.ecommerce.controller;

import com.study.ecommerce.request.MemberSignUp;
import com.study.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberSignUp memberSignUp){
        authService.signup(memberSignUp);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/resign")
    public ResponseEntity<Void> resign(@RequestBody long id){
        authService.resign(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
