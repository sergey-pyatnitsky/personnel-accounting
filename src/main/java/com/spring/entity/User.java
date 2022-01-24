package com.spring.entity;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "active")
    private boolean isActive;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Employee employee;

    public User() {
    }
}
