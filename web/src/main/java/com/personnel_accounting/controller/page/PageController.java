package com.personnel_accounting.controller.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Controller
public class PageController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        return "authorization/authorization";
    }

    @GetMapping("/")
    public String showMainPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        model.addAttribute("title", messageSource.getMessage("main_page.title",
                null, LocaleContextHolder.getLocale()));
        return "main-page/main-page";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        model.addAttribute("title", messageSource.getMessage("navbar.profile",
                null, LocaleContextHolder.getLocale()));
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
        model.addAttribute("title", messageSource.getMessage("sidebar.telephone_directory",
                null, LocaleContextHolder.getLocale()));
        return "telephone-directory";
    }

    @GetMapping("/user")
    public String getUserPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        model.addAttribute("title", messageSource.getMessage("user.title",
                null, LocaleContextHolder.getLocale()));
        return "user";
    }

    @GetMapping("/department")
    public String getDepartmentPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        model.addAttribute("title", messageSource.getMessage("department.title",
                null, LocaleContextHolder.getLocale()));
        return "department";
    }

    @GetMapping("/project")
    public String getProjectPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        model.addAttribute("title", messageSource.getMessage("project.title",
                null, LocaleContextHolder.getLocale()));
        return "project";
    }

    @GetMapping("/task")
    public String getTaskPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        model.addAttribute("title", messageSource.getMessage("task.title",
                null, LocaleContextHolder.getLocale()));
        return "task";
    }

    @GetMapping("/my-task")
    public String getMyTaskPage(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        model.addAttribute("title", messageSource.getMessage("my_task.title",
                null, LocaleContextHolder.getLocale()));
        return "my-task";
    }
}
