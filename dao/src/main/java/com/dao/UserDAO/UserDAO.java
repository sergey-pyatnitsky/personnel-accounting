package com.dao.UserDAO;

import com.core.domain.User;
import com.core.enums.Role;

import java.util.List;

public interface UserDAO {
    User find(int id);
    List<User> findByActive(boolean isActive);
    List<User> findAll();
    User findByUsername(String username);
    List<User> findByRole(Role role);

    User create(User user);
    User update(User user);
    boolean removeById(Long id);
    boolean remove(User user);

    boolean inactivateById(long id);
    boolean inactivate(User user);

    boolean activateById(long id);
    boolean activate(User user);
}
