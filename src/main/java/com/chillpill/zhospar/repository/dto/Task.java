package com.chillpill.zhospar.repository.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.REMOVE)
    private List<Task> subtasks;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Account creator;

    private String description;

    @PrePersist
    public void prePersist() {
        createdAt = Date.valueOf(LocalDate.now());
    }

    @Column(name = "created_at", nullable = false, columnDefinition = "date default CURRENT_DATE")
    private Date createdAt;

    private Date deadline;

    @OneToMany(mappedBy = "task")
    private List<TaskExecution> taskExecutions;
}
