package com.personnel_accounting.controller.restController;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ProfileDTO;
import com.personnel_accounting.entity.dto.UserDTO;
import com.personnel_accounting.exeption.IncorrectDataException;
import com.personnel_accounting.user.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeProfileRESTController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final ConversionService conversionService;

    public EmployeeProfileRESTController(EmployeeService employeeService, UserService userService,
                                         ConversionService conversionService) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PostMapping("/api/employee/profile/check_old_password")
    public ResponseEntity<?> checkOldUserPass(@RequestBody UserDTO userDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(userDTO.getPassword(), user.getPassword().replace("{bcrypt}", "")))
            throw new IncorrectDataException("Неправильный пароль");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/employee/profile/edit_password")
    public ResponseEntity<?> editUserPass(@RequestBody UserDTO userDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        userService.changeAuthData(user, "{bcrypt}" + (new BCryptPasswordEncoder()).encode(userDTO.getPassword()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/employee/profile/get_profile_data")
    public ResponseEntity<?> getProfileData(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Employee employee = employeeService.findByUser(user);
        EmployeeDTO employeeDTO = conversionService.convert(employee, EmployeeDTO.class);
        employeeDTO.setProfile(conversionService.convert(employee.getProfile(), ProfileDTO.class));
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @PostMapping("/api/employee/profile/edit")
    public ResponseEntity<?> editProfileData(@RequestBody EmployeeDTO employeeDTO, Authentication authentication) {
        Employee employee = employeeService.findByUser(userService.findByUsername(authentication.getName()));
        employee.setName(employeeDTO.getName());
        employeeService.addProfileData(employee, conversionService.convert(employeeDTO.getProfile(), Profile.class));
        employeeService.save(employee);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
