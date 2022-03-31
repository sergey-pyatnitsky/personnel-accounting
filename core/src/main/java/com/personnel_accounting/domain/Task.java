package com.personnel_accounting.domain;

import com.personnel_accounting.enums.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private Employee reporter;

    @OneToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private Employee assignee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus taskStatus;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @OneToOne(mappedBy = "task", fetch = FetchType.LAZY)
    private ReportCard reportCard;

    public Task(String name, String description,
                Project project, Employee reporter, Employee assignee, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.project = project;
        this.reporter = reporter;
        this.assignee = assignee;
        this.taskStatus = taskStatus;
        createDate = new Date(System.currentTimeMillis());
    }

    public Task(String name, String description,
                Employee reporter, Employee assignee, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.project = null;
        this.reporter = reporter;
        this.assignee = assignee;
        this.taskStatus = taskStatus;
        createDate = new Date(System.currentTimeMillis());
    }
}