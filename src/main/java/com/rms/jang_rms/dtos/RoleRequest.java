package com.rms.jang_rms.dtos;

import lombok.Data;

import java.util.List;

@Data
public class RoleRequest {
    private String name;
    private List<Long> permissionIds;
    private String description;
}
