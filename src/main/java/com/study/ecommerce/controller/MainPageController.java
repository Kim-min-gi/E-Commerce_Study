package com.study.ecommerce.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainPageController {


    @GetMapping("/")
    public String main(){
        return "mainPage입니다.";
    }


    @GetMapping("/user")
    public String userPage(){
        return "userpage입니다.";
    }




}
