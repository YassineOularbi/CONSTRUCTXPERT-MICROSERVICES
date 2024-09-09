package com.user_service.service;

import com.user_service.dto.SupervisorDto;
import com.user_service.exception.SupervisorNotFoundException;
import com.user_service.mapper.UserMapper;
import com.user_service.model.Supervisor;
import com.user_service.repository.SupervisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SupervisorService {
    
    private final SupervisorRepository supervisorRepository;
    private final UserMapper supervisorMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Supervisor> getAllSupervisors() {
        return supervisorRepository.findAll();
    }

    public Supervisor getSupervisorById(Long id) {
        return supervisorRepository.findById(id).orElseThrow(() -> new SupervisorNotFoundException(id));
    }

    public SupervisorDto updateSupervisor(Long id, SupervisorDto supervisorDto) {
        var existingSupervisor = supervisorRepository.findById(id).orElseThrow(() -> new SupervisorNotFoundException(id));
        var updatedSupervisor = (Supervisor) supervisorMapper.partialUpdate(supervisorDto, existingSupervisor);
        updatedSupervisor.setPassword(passwordEncoder.encode(supervisorDto.getPassword()));
        var savedSupervisor = supervisorRepository.save(updatedSupervisor);
        return (SupervisorDto) supervisorMapper.toDto(savedSupervisor);
    }

    public void deleteSupervisor(Long id) {
        var supervisor = supervisorRepository.findById(id).orElseThrow(() -> new SupervisorNotFoundException(id));
        supervisorRepository.delete(supervisor);
    }
}
