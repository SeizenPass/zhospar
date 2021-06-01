package com.chillpill.zhospar.controller.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private MultipartFile image;
}
