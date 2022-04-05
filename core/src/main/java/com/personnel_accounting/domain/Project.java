package com.personnel_accounting.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @ToString.Exclude
    private Department department;

    @OneToMany(mappedBy = "project")
    @ToString.Exclude
    private List<EmployeePosition> employeePositionList;

    @OneToMany(mappedBy = "project")
    @ToString.Exclude
    private List<Task> taskList;

    public Project(String name, Department department, boolean isActive) {
        this.name = name;
        this.department = department;
        this.isActive = isActive;
        createDate = new Date(System.currentTimeMillis());
    }
}
