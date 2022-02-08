package com.service.user;

import com.core.domain.Employee;
import com.core.domain.Profile;
import com.core.domain.User;
import com.core.enums.Role;
import com.dao.employee.EmployeeDAO;
import com.dao.user.UserDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserDAO userDAO;
    private final EmployeeDAO employeeDAO;

    public UserServiceImpl(UserDAO userDAO, EmployeeDAO employeeDAO) {
        this.userDAO = userDAO;
        this.employeeDAO = employeeDAO;
    }

    @Override
    @Transactional
    public boolean registerUser(String login, String password, Role role, String name) {
        if(userDAO.findByUsername(login) == null) {
            User user = new User(login, password, role, false);
            if(role == Role.SUPER_ADMIN){
                userDAO.save(user);
            }
            else{
                Employee employee = new Employee(name, false, user, new Profile());
                employeeDAO.save(employee);
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public User authorizeUser(String login, String password) {
        User user = userDAO.findByUsername(login);
        if(user.getPassword().equals(password))
            return user;
        return null;
    }

    @Override
    @Transactional
    public User changeUserRole(User user, Role role) {
        user.setRole(role);
        return userDAO.save(user);
    }

    @Override
    @Transactional
    public User changeAuthData(User user, String login, String password) {
        user.setUsername(login);
        user.setUsername(password);
        return userDAO.save(user);
    }

    @Override
    @Transactional
    public User find(Long id) {
        return userDAO.find(id);
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
    @Transactional
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    @Transactional
    public List<User> findByRole(Role role) {
        return userDAO.findByRole(role);
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
}
