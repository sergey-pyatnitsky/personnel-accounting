package com.personnel_accounting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {

    @GetMapping("/telephone-directory")
    public String getTelephoneDirectory(){
        return "telephone-directory";
    }
}
