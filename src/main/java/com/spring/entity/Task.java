package com.spring.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne
    @JoinColumn(name = "reporter_id")
    private Employee reporter;

    @OneToOne
    @JoinColumn(name = "assignee_id")
    private Employee assignee;

    @OneToOne
    @JoinColumn(name = "status_id")
    private TaskStatus taskStatus;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @OneToOne(fetch = FetchType.LAZY)
    private ReportCard reportCard;

    public Task() {
    }
}
