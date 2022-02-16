package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String showLoginPage() {
        return "authorization/authorization";
    }

    @RequestMapping("/registration")
    public String showRegistrationPage() {
        return "authorization/registration";
    }

    @RequestMapping("/main-page")
    public String showMainPage() {
        return "main-page/main-page";
    }

    @RequestMapping("/profile")
    public String showProfile(){
        return "profile-page";
    }
}
