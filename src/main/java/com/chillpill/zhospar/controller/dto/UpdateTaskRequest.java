package com.chillpill.zhospar.controller.dto;

import lombok.Data;

@Data
public class UpdateTaskRequest {
    private long taskId;
    private String title;
    private String description;
    private String deadline;
}
