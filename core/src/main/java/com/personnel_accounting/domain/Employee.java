package com.personnel_accounting.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH}
            , fetch = FetchType.LAZY)
    @JoinColumn(name = "username", unique = true, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @OneToMany(mappedBy = "employee")
    private List<EmployeePosition> employeePositionList;

    @OneToMany(mappedBy = "employee")
    private List<ReportCard> reportCardList;

    public Employee(String name, boolean isActive, User user, Profile profile) {
        this.user = user;
        this.profile = profile;
        this.name = name;
        this.isActive = isActive;
        createDate = new Date(System.currentTimeMillis());
    }

    public Employee(String name, boolean isActive, User user) {
        this.user = user;
        profile = null;
        this.name = name;
        this.isActive = isActive;
        createDate = new Date(System.currentTimeMillis());
        profile = new Profile();
    }
}
