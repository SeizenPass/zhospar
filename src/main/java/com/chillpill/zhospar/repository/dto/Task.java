package com.chillpill.zhospar.repository.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Task parentTask;

    @OneToMany(mappedBy = "parentTask")
    private List<Task> subtasks;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TaskStatus status;

    @OneToMany(mappedBy = "task")
    private List<TaskExecution> taskExecutions;
}
