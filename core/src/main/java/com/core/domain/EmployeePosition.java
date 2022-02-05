package com.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee_position")
public class EmployeePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    public EmployeePosition(boolean isActive, Employee employee, Position position, Project project, Department department) {
        this.isActive = isActive;
        this.employee = employee;
        this.position = position;
        this.project = project;
        this.department = department;
        createDate = new Date(System.currentTimeMillis());
    }
}
