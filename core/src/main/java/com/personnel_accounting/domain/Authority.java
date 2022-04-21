package com.personnel_accounting.domain;

import com.personnel_accounting.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

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
