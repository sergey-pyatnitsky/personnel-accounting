package com.personnel_accounting.authority;

import com.personnel_accounting.domain.Authority;
import com.personnel_accounting.enums.Role;

import java.util.List;

public interface AuthorityDAO {
    Authority find(String username);
    List<Authority> findAll();
    List<Authority> findByRole(Role role);

    Authority save(Authority authority);
    Authority merge(Authority authority);
    boolean removeByUsername(String username);
    boolean remove(Authority authority);
}
