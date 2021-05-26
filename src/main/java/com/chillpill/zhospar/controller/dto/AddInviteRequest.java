package com.chillpill.zhospar.controller.dto;

import lombok.Data;

@Data
public class AddInviteRequest {
    private long projectId;
    private String credentials;
}
