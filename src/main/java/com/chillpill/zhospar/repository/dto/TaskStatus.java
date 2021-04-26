package com.chillpill.zhospar.repository.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusId;

    @Column(nullable = false)
    private String statusName;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "status")
    private List<Task> taskList;
}
