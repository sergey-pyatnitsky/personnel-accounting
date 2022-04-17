package com.personnel_accounting.controller.page;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

@Controller
public class PageController {

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "authorization/authorization";
    }

    @GetMapping("/")
    public String showMainPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "main-page/main-page";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "profile-page";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "authorization/registration";
    }

    @GetMapping("/telephone-directory")
    public String getTelephoneDirectoryPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "telephone-directory";
    }

    @GetMapping("/user")
    public String getUserPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "user";
    }

    @GetMapping("/department")
    public String getDepartmentPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "department";
    }

    @GetMapping("/project")
    public String getProjectPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "project";
    }

    @GetMapping("/task")
    public String getTaskPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "task";
    }

    @GetMapping("/my-task")
    public String getMyTaskPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "my-task";
    }
}
