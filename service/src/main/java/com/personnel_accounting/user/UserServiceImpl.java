package com.personnel_accounting.user;

import com.personnel_accounting.domain.Authority;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.authority.AuthorityDAO;
import com.personnel_accounting.employee.EmployeeDAO;
import com.personnel_accounting.utils.ValidationUtil;
import com.personnel_accounting.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public Authority getAuthorityByUsername(String username) {
        return authorityDAO.find(username);
    }

    @Override
    public User changeAuthData(User user, String password) {
        ValidationUtil.validate(user, userValidator);
        if (!Objects.equals(user.getPassword(), password)) {
            user.setPassword(password);
            return userDAO.save(user);
        }
        return userDAO.merge(user);
    }

    @Override
    public User changeUserRole(User user, Role role) {
        Authority authority = authorityDAO.find(user.getUsername());
        if (authority.getRole() != role) {
            authority.setRole(role);
            authorityDAO.save(authority);
        }
        return userDAO.merge(user);
    }

    @Override
    public boolean registerUser(User user, String name, Role role) { //FIXME test
        ValidationUtil.validate(user, userValidator);
        User tempUser = userDAO.find(user.getUsername());
        if (tempUser == null) {
            user = userDAO.save(user);
            authorityDAO.save(new Authority(user.getUsername(), role));
            if (!role.equals(Role.SUPER_ADMIN)) {
                Employee employee = new Employee(name, false, user, new Profile());
                employeeDAO.save(employee);
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
    public Role findRoleByUsername(String username) { //TODO test
        return authorityDAO.find(username).getRole();
    }

    @Override
    public boolean inactivate(User user) {
        return userDAO.inactivate(user);
    }

    @Override
    public boolean activate(User user) {
        return userDAO.activate(user);
    }

    @Override
    public boolean remove(User user) {
        if (authorityDAO.removeByUsername(user.getUsername()))
            return userDAO.remove(user);
        return false;

    }

    @Override
    public User save(User user) {
        ValidationUtil.validate(user, userValidator);
        return userDAO.save(user);
    }

    @Override
    public User save(User user, Role role) {
        ValidationUtil.validate(user, userValidator);
        Authority authority = new Authority(user.getUsername(), role);
        user = userDAO.save(user);
        authorityDAO.save(authority);
        return user;

    }
}
