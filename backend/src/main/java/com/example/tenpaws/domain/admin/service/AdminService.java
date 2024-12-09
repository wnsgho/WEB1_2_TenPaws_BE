package com.example.tenpaws.domain.admin.service;

import com.example.tenpaws.domain.admin.dto.AdminRequestDTO;
import com.example.tenpaws.domain.admin.dto.AdminResponseDTO;
import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.domain.admin.repository.AdminRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO) {

        Admin admin = adminRequestDTO.toEntity();
        admin.changePassword(bCryptPasswordEncoder.encode(adminRequestDTO.getPassword()));

        Admin savedAdmin = adminRepository.save(admin);
        return AdminResponseDTO.fromEntity(savedAdmin);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.ADMIN_NOT_FOUND));

        adminRepository.delete(admin);
    }

    @Transactional
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO adminRequestDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.ADMIN_NOT_FOUND));

        if (adminRequestDTO.getUsername() != null) {
            admin.changeUsername(adminRequestDTO.getUsername());
        }

        if (adminRequestDTO.getPassword() != null) {
            admin.changePassword(bCryptPasswordEncoder.encode(adminRequestDTO.getPassword()));
        }

        if (adminRequestDTO.getEmail() != null) {
            admin.changeEmail(adminRequestDTO.getEmail());
        }

        return AdminResponseDTO.fromEntity(admin);
    }

    @Transactional(readOnly = true)
    public AdminResponseDTO getAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.ADMIN_NOT_FOUND));

        return AdminResponseDTO.fromEntity(admin);
    }

    public boolean isAdminOwn(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        Admin admin = adminRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new BaseException(ErrorCode.ADMIN_NOT_FOUND));

        if (!admin.getId().equals(id)) {
            throw new BaseException(ErrorCode.ADMIN_NOT_AUTHORIZED);
        }

        return true;
    }
}
