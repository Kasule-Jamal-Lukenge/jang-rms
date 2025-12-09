package com.rms.jang_rms.modules.permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String name);
}
