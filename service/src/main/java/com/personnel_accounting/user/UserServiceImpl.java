package com.personnel_accounting.user;

import com.personnel_accounting.authority.AuthorityDAO;
import com.personnel_accounting.domain.Authority;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.email.EmailService;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.profile.ProfileDAO;
import com.personnel_accounting.utils.ValidationUtil;
import com.personnel_accounting.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private AuthorityDAO authorityDAO;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProfileDAO profileDAO;

    @Autowired
    private MessageSource messageSource;

    @Override
    public Authority getAuthorityByUsername(String username) {
        return authorityDAO.find(username);
    }

    @Override
    public User changeAuthData(User user, String password) {
        ValidationUtil.validate(user, userValidator);
        user.setPassword(password);
        return userDAO.save(user);
    }

    @Override
    @Transactional
    public User changeUserRole(User user, Role role) {
        Authority authority = authorityDAO.find(user.getUsername());
        if (authority.getRole() != role) {
            authority.setRole(role);
            authorityDAO.save(authority);
        }
        return userDAO.merge(user);
    }

    @Override
    @Transactional
    public boolean registerUser(User user, String pass, String name, Role role, String email) {
        ValidationUtil.validate(user, userValidator);
        user.setPassword(pass);
        User tempUser = userDAO.find(user.getUsername());
        if (tempUser == null) {
            user = userDAO.save(user);
            authorityDAO.save(new Authority(user.getUsername(), role));
            if (!role.equals(Role.SUPER_ADMIN)) {
                Employee employee = new Employee(name, false, user, new Profile());
                Profile profile = new Profile();
                profile.setEmail(email);
                profile.setImageId("1oRfzcWiifuIhZOh4h5eqVU2REr1G_EQ-");
                profile = profileDAO.save(profile);
                employee.setProfile(profile);
                employeeDAO.save(employee);
                emailService.sendSimpleEmail(email,
                        messageSource.getMessage("email.welcome.title", null, null),
                        messageSource.getMessage("email.welcome.message", null, null));
            }
            return true;
        } else return false;
    }

    @Override
    public User find(String username) {
        return userDAO.find(username);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.find(username);
    }

    @Override
    public List<User> findByRole(Role role) {
        List<Authority> authorities = authorityDAO.findByRole(role);
        List<User> users = new ArrayList<>();
        authorities.forEach(obj -> users.add(userDAO.find(obj.getUsername())));
        return users;
    }

    @Override
    public Role findRoleByUsername(String username) {
        return authorityDAO.find(username).getRole();
    }

    @Override
    @Transactional
    public boolean inactivate(User user) {
        boolean isActive = userDAO.inactivate(user);
        if (isActive) {
            employeeDAO.findByUser(user).getProfile().getEmail();
            emailService.sendSimpleEmail(employeeDAO.findByUser(user).getProfile().getEmail(),
                    messageSource.getMessage("email.deactivate.success.title", null, null),
                    messageSource.getMessage("email.deactivate.success.message", null, null));
        }
        return isActive;
    }

    @Override
    @Transactional
    public boolean activate(User user) {
        boolean isActive = userDAO.activate(user);
        if (isActive) {
            employeeDAO.findByUser(user).getProfile().getEmail();
            emailService.sendSimpleEmail(employeeDAO.findByUser(user).getProfile().getEmail(),
                    messageSource.getMessage("email.activate.success.title", null, null),
                    messageSource.getMessage("email.activate.success.message", null, null));
        }
        return isActive;
    }

    @Override
    @Transactional
    public boolean remove(User user) {
        if (authorityDAO.removeByUsername(user.getUsername())) {
            return userDAO.remove(userDAO.find(user.getUsername()));
        }
        return false;

    }

    @Override
    public User save(User user) {
        ValidationUtil.validate(user, userValidator);
        return userDAO.save(user);
    }

    @Override
    @Transactional
    public User save(User user, Role role) {
        ValidationUtil.validate(user, userValidator);
        Authority authority = new Authority(user.getUsername(), role);
        user = userDAO.save(user);
        authorityDAO.save(authority);
        return user;

    }
}
