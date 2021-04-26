package com.chillpill.zhospar.repository.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TaskExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long linkId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
