package com.service.user;

import com.core.domain.Authority;
import com.core.domain.Employee;
import com.core.domain.Profile;
import com.core.domain.User;
import com.core.enums.Role;
import com.dao.authority.AuthorityDAO;
import com.dao.employee.EmployeeDAO;
import com.dao.user.UserDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final EmployeeDAO employeeDAO;
    private final AuthorityDAO authorityDAO;

    public UserServiceImpl(UserDAO userDAO, EmployeeDAO employeeDAO, AuthorityDAO authorityDAO) {
        this.userDAO = userDAO;
        this.employeeDAO = employeeDAO;
        this.authorityDAO = authorityDAO;
    }

    @Override
    @Transactional
    public User changeAuthData(User user, String login, String password) {
        user.setUsername(login);
        user.setPassword(password);
        return userDAO.save(user);
    }

    @Override
    @Transactional
    public User find(String username) {
        return userDAO.find(username);
    }

    @Override
    @Transactional
    public List<User> findByActive(boolean isActive) {
        return userDAO.findByActive(isActive);
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.find(username);
    }

    @Override
    @Transactional
    public List<User> findByRole(Role role) {
        List<Authority> authorities = authorityDAO.findByRole(role);
        List<User> users = new ArrayList<>();
        authorities.forEach(obj -> {
            users.add(userDAO.find(obj.getUsername()));
        });
        return users;
    }


    @Override
    @Transactional
    public boolean inactivate(User user) {
        return userDAO.inactivate(user);
    }

    @Override
    @Transactional
    public boolean activate(User user) {
        return userDAO.activate(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        return userDAO.update(user);
    }

    @Override
    @Transactional
    public boolean remove(User user) {
        return userDAO.remove(user);
    }

    @Override
    @Transactional
    public User save(User user, Role role) {

        user.getAuthorityList().add(authorityDAO.save(new Authority(user.getUsername(), role)));
        return userDAO.save(user);
    }
}
