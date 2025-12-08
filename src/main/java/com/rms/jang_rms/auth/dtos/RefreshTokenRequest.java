package com.rms.jang_rms.auth.dtos;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
