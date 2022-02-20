package com.dao.authority;

import com.core.domain.Authority;
import com.core.enums.Role;

import java.util.List;

public interface AuthorityDAO {
    Authority find(String username);
    List<Authority> findAll();
    List<Authority> findByRole(Role role);

    Authority save(Authority authority);
    Authority update(Authority authority);
    boolean removeByUsername(String username);
    boolean remove(Authority authority);
}
