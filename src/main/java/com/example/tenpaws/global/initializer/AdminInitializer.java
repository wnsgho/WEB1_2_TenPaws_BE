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
        if (adminRepository.countByUserRole(UserRole.ROLE_ADMIN) > 0) {
            return;
        }

        List<Admin> admins = List.of(
                new Admin("TENPAWS_ADMIN_1", bCryptPasswordEncoder.encode("tenpaws1"), "tenpaws1@tenpaws.com", UserRole.ROLE_ADMIN),
                new Admin("TENPAWS_ADMIN_2", bCryptPasswordEncoder.encode("tenpaws2"), "tenpaws2@tenpaws.com", UserRole.ROLE_ADMIN),
                new Admin("TENPAWS_ADMIN_3", bCryptPasswordEncoder.encode("tenpaws3"), "tenpaws3@tenpaws.com", UserRole.ROLE_ADMIN)
        );

        adminRepository.saveAll(admins);
    }
}
