package com.web.entity.dto;

import lombok.Data;

@Data
public class ProfileDTO {
    private Long id;
    private String education;
    private String address;
    private String phone;
    private String email;
    private String skills;
}
