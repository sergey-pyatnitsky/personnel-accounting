package com.personnel_accounting.user;

import com.personnel_accounting.domain.Authority;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.enums.Role;

import java.util.List;

public interface UserService {
    Authority getAuthorityByUsername(String username);

    User changeAuthData(User user, String password);
    User changeUserRole(User user, Role role);
    boolean registerUser(User user, String pass, String name, Role role, String email);

    User find(String username);
    List<User> findAll();
    User findByUsername(String username);
    List<User> findByRole(Role role);
    Role findRoleByUsername(String username);

    boolean inactivate(User user);
    boolean activate(User user);

    User save(User user);
    User save(User user, Role role);
    boolean remove(User user);
}
