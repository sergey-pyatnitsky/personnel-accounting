package com.service.user;

import com.core.domain.User;
import com.core.enums.Role;

import java.util.List;

public interface UserService {
    boolean registerUser(String login, String password, Role role, String name);
    User authorizeUser(String login, String password);
    User changeUserRole(User user, Role role);
    User changeAuthData(User user, String login, String password);

    User find(Long id);
    List<User> findByActive(boolean isActive);
    List<User> findAll();
    User findByUsername(String username);
    List<User> findByRole(Role role);

    boolean inactivate(User user);
    boolean activate(User user);

    User save(User user);
    User update(User user);
    boolean remove(User user);
}
