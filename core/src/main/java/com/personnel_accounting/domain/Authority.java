package com.personnel_accounting.domain;

import com.personnel_accounting.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column(name = "username", nullable = false)

    private String username;

    @Column(name = "authority", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public Authority(String username, Role role) {
        this.username = username;
        this.role = role;
    }
}
