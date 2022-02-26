package com.personnel_accounting.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = true, nullable = false)
    private String password;

    @Column(name = "enabled")
    private boolean isActive;

    /*@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<Authority> authorityList = new ArrayList<>();*/

    public User(String username, String password, boolean isActive) {
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    /*public User(String username, String password, boolean isActive, List<Authority> authorityList) {
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.authorityList = authorityList;
    }*/
}
