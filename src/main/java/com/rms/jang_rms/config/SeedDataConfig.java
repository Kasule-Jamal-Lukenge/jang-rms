package com.rms.jang_rms.config;

import com.rms.jang_rms.modules.permission.Permission;
import com.rms.jang_rms.modules.permission.PermissionRepository;
import com.rms.jang_rms.modules.role.Role;
import com.rms.jang_rms.modules.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SeedDataConfig implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final PermissionRepository permRepo;

    @Override
    public void run(String... args) {

        // 1️⃣ Seed permissions ONLY if empty
        if (permRepo.count() == 0) {
            permRepo.save(Permission.builder()
                    .name("PROPERTY_VIEW")
                    .description("Can view properties")
                    .build());

            permRepo.save(Permission.builder()
                    .name("PROPERTY_CREATE")
                    .description("Can create new properties")
                    .build());

            permRepo.save(Permission.builder()
                    .name("BOOKING_APPROVE")
                    .description("Can approve bookings")
                    .build());

            permRepo.save(Permission.builder()
                    .name("PAYMENT_REFUND")
                    .description("Can refund payments")
                    .build());

            permRepo.save(Permission.builder()
                    .name("ADD_USERS")
                    .description("Can add Users")
                    .build());
        }

        // 2️⃣ Seed roles ONLY if empty
        if (roleRepo.count() == 0) {
            List<Permission> allPerms = permRepo.findAll();
            Set<Permission> adminPerms = new HashSet<>(allPerms);

            Role admin = Role.builder()
                    .name("ADMIN")
                    .description("System Administrator")
                    .permissions(adminPerms)
                    .build();

            roleRepo.save(admin);

            Role publicUser = Role.builder()
                    .name("PUBLIC_USER")
                    .description("Public User")
                    .permissions(new HashSet<>())
                    .build();

            roleRepo.save(publicUser);
        }
    }
}
