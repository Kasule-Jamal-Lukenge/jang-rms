package com.rms.jang_rms.auth.dtos;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
