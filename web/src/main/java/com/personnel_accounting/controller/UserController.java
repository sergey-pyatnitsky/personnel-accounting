package com.personnel_accounting.controller;

import com.personnel_accounting.domain.User;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.user.UserService;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;
    private final ConversionService conversionService;

    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        model.addAttribute("employee", employeeDTO);

        return "authorization/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("employee")EmployeeDTO employeeDTO){
        employeeDTO.getUser().setActive(true);
        employeeDTO.getUser().setPassword("{bcrypt}" +
                (new BCryptPasswordEncoder()).encode(employeeDTO.getUser().getPassword()));
        User user = userService.registerUser(conversionService.convert(employeeDTO.getUser(), User.class),
                employeeDTO.getName(), Role.EMPLOYEE);
        return "redirect:/";
    }

    @GetMapping("/telephone-directory")
    public String getTelephoneDirectory(){
        return "telephone-directory";
    }
}
