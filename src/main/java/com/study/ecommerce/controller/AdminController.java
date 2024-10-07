package com.study.ecommerce.controller;

import com.study.ecommerce.exception.AdminCodeNotMatch;
import com.study.ecommerce.request.MemberSignUp;
import com.study.ecommerce.response.MemberResponse;
import com.study.ecommerce.service.AdminService;
import com.study.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<MemberResponse>> member(@PageableDefault Pageable pageable) throws Exception{
        List<MemberResponse> memberRespons = memberService.getMembers(pageable);
        return ResponseEntity.ok(memberRespons);
    }

    @GetMapping("/admin/member/{id}")
    public ResponseEntity<MemberResponse> getmember(@PathVariable long id) throws Exception{
        MemberResponse memberResponse = memberService.getMember(id);
        return ResponseEntity.ok(memberResponse);
    }


    @PostMapping("/auth/signup/admin")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberSignUp memberSignUp){

        if (!memberSignUp.getCode().equals(adminCode)){
            throw new AdminCodeNotMatch();
        }

        authService.adminSignup(memberSignUp);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }








}
