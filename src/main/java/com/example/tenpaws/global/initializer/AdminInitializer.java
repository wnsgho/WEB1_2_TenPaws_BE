package com.example.tenpaws.global.initializer;

import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.domain.admin.repository.AdminRepository;
import com.example.tenpaws.global.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.countByUserRole(UserRole.ROLE_SUPER_ADMIN) > 0) {
            return;
        }

        List<Admin> admins = List.of(
                new Admin("superadmin", bCryptPasswordEncoder.encode("superadmin123"), "superadmin@tenpaws.com", UserRole.ROLE_SUPER_ADMIN),
                new Admin("admin1", bCryptPasswordEncoder.encode("admin123"), "admin1@tenpaws.com", UserRole.ROLE_ADMIN),
                new Admin("admin2", bCryptPasswordEncoder.encode("admin123"), "admin2@tenpaws.com", UserRole.ROLE_ADMIN)
        );

        adminRepository.saveAll(admins);
    }
}
