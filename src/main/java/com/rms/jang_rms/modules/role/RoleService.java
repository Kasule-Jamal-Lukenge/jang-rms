package com.rms.jang_rms.modules.role;

import com.rms.jang_rms.modules.permission.Permission;
import com.rms.jang_rms.modules.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    @Autowired
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public Role createRole(String name, List<Long> permissionIds, String description) {
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        Set<Permission> permissionsSet = new HashSet<>(permissions);

        Role role =  Role.builder()
                .name(name)
                .permissions(permissionsSet)
                .description(description)
                .build();
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, String name, List<Long> permissionIds, String description) {
        Role role = roleRepository.findById(id).orElseThrow(()-> new RuntimeException("Role not found"));

        role.setName(name);

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        Set<Permission> permissionsSet = new HashSet<>(permissions);
        role.setPermissions(permissionsSet);

        role.setDescription(description);

        return roleRepository.save(role);
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
