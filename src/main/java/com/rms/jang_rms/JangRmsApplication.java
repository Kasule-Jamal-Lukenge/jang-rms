package com.rms.jang_rms;

import com.rms.jang_rms.modules.permission.Permission;
import com.rms.jang_rms.modules.permission.PermissionRepository;
import com.rms.jang_rms.modules.role.Role;
import com.rms.jang_rms.modules.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class JangRmsApplication {

	@Bean
	public CommandLineRunner loadDefaultRoles(
			RoleRepository roleRepo,
			PermissionRepository permRepo
	) {
		return args -> {

			if (permRepo.count() == 0) {
				permRepo.save(new Permission(null, "PROPERTY_VIEW", "allows viewing all properties"));
				permRepo.save(new Permission(null, "PROPERTY_CREATE", "allows one to add a property"));
				permRepo.save(new Permission(null, "BOOKING_APPROVE", "allows users to approve a booking"));
				permRepo.save(new Permission(null, "PAYMENT_REFUND", "allows payment refunds"));
			}

			if (roleRepo.count() == 0) {
				List<Permission> adminPerms = permRepo.findAll();
				Set<Permission> adminPermsSet = new HashSet<>(adminPerms);

				Role admin = Role.builder()
						.name("ADMIN")
						.permissions(adminPermsSet)
						.description("Administrator")
						.build();

				roleRepo.save(admin);

				Role publicUser = Role.builder()
						.name("PUBLIC_USER")
						.permissions(Set.of()) // No permissions
						.description("Public User")
						.build();

				roleRepo.save(publicUser);
			}
		};
	}


	public static void main(String[] args) {
		SpringApplication.run(JangRmsApplication.class, args);
	}

}
