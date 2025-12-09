package com.rms.jang_rms.dtos;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String contact;
    private String occupation;
    private String currentResidence;
}
