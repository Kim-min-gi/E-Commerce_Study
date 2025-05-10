package com.study.ecommerce.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class MainPageController {


    @GetMapping("/")
    public String main(){
        return "mainPage";
    }

//    @GetMapping("/user")
//    public String userPage(){
//        return "userPage입니다.";
//    }
//
//    @GetMapping("/admin")
//    public String adminPage(){
//        return "adminPage입니다.";
//    }

    @GetMapping("/loginPage")
    public String loginPage() { return "loginPage"; }

    @GetMapping("/cartPage")
    public String cartPage() { return "cartPage"; }

    @GetMapping("/signUpPage")
    public String signUp() { return "signUp"; }



}
