package com.service.user;

import com.core.domain.User;
import com.core.enums.Role;

import java.util.List;

public interface UserService {
    User changeAuthData(User user, String password);
    User changeUserRole(User user, Role role);
    User registerUser(User user, String name, Role role);

    User find(String username);
    List<User> findByActive(boolean isActive);
    List<User> findAll();
    User findByUsername(String username);
    List<User> findByRole(Role role);

    boolean inactivate(User user);
    boolean activate(User user);

    User save(User user);
    User save(User user, Role role);
    User update(User user);
    boolean remove(User user);
}
