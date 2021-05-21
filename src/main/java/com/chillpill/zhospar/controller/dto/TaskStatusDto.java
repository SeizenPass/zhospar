package com.chillpill.zhospar.controller.dto;

import lombok.Data;

@Data
public class TaskStatusDto {
    private long projectId;
    private long statusId;
    private String statusName;
}
