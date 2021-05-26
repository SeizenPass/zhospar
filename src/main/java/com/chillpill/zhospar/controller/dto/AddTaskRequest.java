package com.chillpill.zhospar.controller.dto;

import lombok.Data;

@Data
public class AddTaskRequest {
    private String taskName;
    private long projectId;
    private String deadline;
    private long parentId;
    private long statusId;
    private long[] executors;
}
