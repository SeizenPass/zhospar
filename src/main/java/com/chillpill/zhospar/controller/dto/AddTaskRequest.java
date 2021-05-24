package com.chillpill.zhospar.controller.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class AddTaskRequest {
    private String taskName;
    private long projectId;
    private Date deadline;
    private long parentId;
    private long statusId;
}
