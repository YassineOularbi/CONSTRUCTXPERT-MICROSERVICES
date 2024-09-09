package com.user_service.service;

import com.user_service.dto.AdminDto;
import com.user_service.exception.AdminNotFoundException;
import com.user_service.mapper.UserMapper;
import com.user_service.model.Admin;
import com.user_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
    }

    public AdminDto updateAdmin(Long id, AdminDto adminDto) {
        var existingAdmin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
        var updatedAdmin = (Admin) adminMapper.partialUpdate(adminDto, existingAdmin);
        updatedAdmin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        var savedAdmin = adminRepository.save(updatedAdmin);
        return (AdminDto) adminMapper.toDto(savedAdmin);
    }

    public void deleteAdmin(Long id) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
        adminRepository.delete(admin);
    }
}
