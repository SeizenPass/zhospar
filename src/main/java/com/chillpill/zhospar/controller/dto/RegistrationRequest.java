package com.chillpill.zhospar.controller.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String fullName;
    private String password;
    private String email;
}
