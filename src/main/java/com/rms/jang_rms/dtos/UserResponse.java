package com.rms.jang_rms.dtos;

import com.rms.jang_rms.enums.Gender;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private String occupation;
    private String currentResidence;
    private Gender gender;
    private String roleName;
}
