package com.chillpill.zhospar.controller.dto;

import lombok.Data;

@Data
public class AddTaskRequest {
    private String taskName;
    private long projectId;
    private long statusId;
}
