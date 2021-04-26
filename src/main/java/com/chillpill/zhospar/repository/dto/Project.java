package com.chillpill.zhospar.repository.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long projectId;

    @Column(nullable = false)
    private String projectName;

    private String projectDescription;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @OneToMany(mappedBy = "project")
    private List<TaskStatus> taskStatuses;

    @OneToMany(mappedBy = "account")
    private List<ProjectMembership> memberships;
}
