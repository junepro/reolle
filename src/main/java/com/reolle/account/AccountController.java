package com.reolle.account;


import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {


    @GetMapping("/sign-up")
    public String signUpForm(Banner.Mode mode) {
        return "account/sign-up";
    }
}

