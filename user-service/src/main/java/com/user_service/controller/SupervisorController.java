package com.user_service.controller;

import com.user_service.dto.SupervisorDto;
import com.user_service.service.SupervisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supervisor")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SupervisorController {

    private final SupervisorService supervisorService;

    @GetMapping("/get-all-supervisors")
    public ResponseEntity<?> getAllSupervisors() {
        try {
            var supervisors = supervisorService.getAllSupervisors();
            return ResponseEntity.ok(supervisors);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-supervisor-by-id/{id}")
    public ResponseEntity<?> getSupervisorById(@PathVariable("id") String id) {
        try {
            var supervisor = supervisorService.getSupervisorById(Long.valueOf(id));
            return ResponseEntity.ok(supervisor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update-supervisor/{id}")
    public ResponseEntity<?> updateSupervisor(@PathVariable("id") String id, @RequestBody SupervisorDto supervisorDto) {
        try {
            var updatedSupervisor = supervisorService.updateSupervisor(Long.valueOf(id), supervisorDto);
            return ResponseEntity.ok(updatedSupervisor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-supervisor/{id}")
    public ResponseEntity<?> deleteSupervisor(@PathVariable("id") String id) {
        try {
            supervisorService.deleteSupervisor(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
