package com.dao.user;

import com.core.domain.User;
import com.core.enums.Role;

import java.util.List;

public interface UserDAO {
    User find(Long id);
    List<User> findByActive(boolean isActive);
    List<User> findAll();
    User findByUsername(String username);
    List<User> findByRole(Role role);

    User save(User user);
    User update(User user);
    boolean removeById(Long id);
    boolean remove(User user);

    boolean inactivateById(Long id);
    boolean inactivate(User user);

    boolean activateById(Long id);
    boolean activate(User user);
}
