package com.personnel_accounting.controller.rest;

import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.entity.dto.EmployeeDTO;
import com.personnel_accounting.entity.dto.ProfileDTO;
import com.personnel_accounting.entity.dto.UserDTO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.exception.IncorrectDataException;
import com.personnel_accounting.user.UserService;
import com.personnel_accounting.utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class EmployeeProfileRESTController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/employee/profile/check_old_password")
    public ResponseEntity<?> checkOldUserPass(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
        User user = userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword().replace("{bcrypt}", "")))
            throw new IncorrectDataException(messageSource.getMessage("user.error.password", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/employee/profile/edit_password")
    public ResponseEntity<?> editUserPass(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
        User user = userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication));
        userService.changeAuthData(user, userDTO.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/employee/profile/get_profile_data")
    public ResponseEntity<?> getProfileData(Authentication authentication) {
        User user = userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication));
        if(!user.getAuthority().getRole().equals(Role.SUPER_ADMIN)) {
            Employee employee = employeeService.findByUser(user);
            EmployeeDTO employeeDTO = conversionService.convert(employee, EmployeeDTO.class);
            employeeDTO.setProfile(conversionService.convert(employee.getProfile(), ProfileDTO.class));
            employeeDTO.setUser(conversionService.convert(user, UserDTO.class));
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(conversionService.convert(user, UserDTO.class), HttpStatus.OK);
    }

    @PostMapping("/employee/profile/edit")
    public ResponseEntity<?> editProfileData(@Valid @RequestBody EmployeeDTO employeeDTO, Authentication authentication) {
        Employee employee = employeeService.findByUser(
                userService.findByUsername(AuthenticationUtil.getUsernameFromAuthentication(authentication)));
        employee.setName(employeeDTO.getName());
        employeeService.addProfileData(employee, conversionService.convert(employeeDTO.getProfile(), Profile.class));
        employeeService.save(employee);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
