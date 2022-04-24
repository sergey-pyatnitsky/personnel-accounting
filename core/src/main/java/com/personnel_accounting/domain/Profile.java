package com.personnel_accounting.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "education")
    private String education;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "skills")
    private String skills;

    @Column(name = "image_id")
    private Long imageId;

    public Profile(String education, String address, String phone, String email, String skills) {
        this.education = education;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.skills = skills;
    }
}
