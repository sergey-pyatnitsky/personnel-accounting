package com.core.domain;

import com.core.enums.TaskStatus;
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

    @OneToOne(fetch = FetchType.LAZY)
    private ReportCard reportCard;
}
