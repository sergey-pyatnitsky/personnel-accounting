package com.personnel_accounting.controller.pageController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "authorization/authorization";
    }

    @GetMapping("/logout")
    public String showLogoutPage() {
        return "authorization/authorization";
    }

    @GetMapping("/")
    public String showMainPage() {
        return "main-page/main-page";
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile-page";
    }

    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "authorization/registration";
    }

    @GetMapping("/telephone-directory")
    public String getTelephoneDirectoryPage() {
        return "telephone-directory";
    }

    @GetMapping("/user")
    public String getUserPage() {
        return "user";
    }

    @GetMapping("/department")
    public String getDepartmentPage() {
        return "department";
    }

    @GetMapping("/project")
    public String getProjectPage() {
        return "project";
    }

    @GetMapping("/task")
    public String getTaskPage() {
        return "task";
    }

    @GetMapping("/my-task")
    public String getMyTaskPage(){
        return "my-task";
    }
}
