package com.rms.jang_rms.modules.role;

import com.rms.jang_rms.dtos.RoleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    @Autowired
    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request.getName(), request.getPermissionIds(), request.getDescription()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.updateRole(id, request.getName(), request.getPermissionIds(), request.getDescription()));
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role Deleted Successfully");
    }
}
