package com.rms.jang_rms.config;

import com.rms.jang_rms.modules.role.Role;
import com.rms.jang_rms.modules.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// CommandLineRunner To Auto-Create The ADMIN, TENANT AND PUBLIC USER_ROLES IN THE DATABASE
@Configuration
@RequiredArgsConstructor
public class SeedDataConfig {
    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {

            if (roleRepository.existsByName("PUBLIC_USER")) {
                roleRepository.save(Role.builder().name("PUBLIC_USER").build());
            }

            if (roleRepository.existsByName("TENANT")) {
                roleRepository.save(Role.builder().name("TENANT").build());
            }

            if (roleRepository.existsByName("ADMIN")) {
                roleRepository.save(Role.builder().name("ADMIN").build());
            }
        };
    }
}
