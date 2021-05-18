package com.chillpill.zhospar.controller.dto;

import lombok.Data;

@Data
public class AddTaskStatusRequest {
    private String statusName;
    private long projectId;
}
