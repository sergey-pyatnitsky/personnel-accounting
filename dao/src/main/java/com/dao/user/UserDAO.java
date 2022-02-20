package com.dao.user;

import com.core.domain.User;

import java.util.List;

public interface UserDAO {
    User find(String username);
    List<User> findByActive(boolean isActive);
    List<User> findAll();

    User save(User user);
    User update(User user);
    boolean removeByUsername(String username);
    boolean remove(User user);

    boolean inactivateByUsername(String username);
    boolean inactivate(User user);

    boolean activateByUsername(String username);
    boolean activate(User user);
}
