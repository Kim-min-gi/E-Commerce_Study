package com.study.ecommerce.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainPageController {


    @GetMapping("/")
    public String main(){
        return "mainPage";
    }

    @GetMapping("/loginPage")
    public String loginPage() { return "loginPage"; }

    @GetMapping("/productPage")
    public String productPage() { return "productPage"; }

    @GetMapping("/orderPage")
    public String orderPage() { return "orderPage"; }

    @GetMapping("/cartPage")
    public String cartPage() { return "cartPage"; }

    @GetMapping("/signUpPage")
    public String signUp() { return "signUp"; }




}
