package com.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToMany(mappedBy = "department")
    private List<Employee> employeeList;

    @OneToMany(mappedBy = "department")
    private List<Project> projectList;

    public Department(String name, boolean isActive, Date startDate) {
        this.name = name;
        this.isActive = isActive;
        this.startDate = startDate;
    }
}
