package com.spring.entity;

import javax.persistence.*;

@Entity
@Table(name = "tack_status")
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public TaskStatus() {
    }
}
