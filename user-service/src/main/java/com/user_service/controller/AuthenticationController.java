package com.user_service.controller;

import com.user_service.dto.AdminDto;
import com.user_service.dto.AuthenticationRequest;
import com.user_service.dto.ClientDto;
import com.user_service.dto.SupervisorDto;
import com.user_service.exception.RegistrationException;
import com.user_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            var response = authenticationService.login(authenticationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(STR."Invalid credentials : \{e.getMessage()}");
        }
    }
    @PostMapping("/register/admin")
    public ResponseEntity<?> adminRegister(@RequestBody AdminDto registerRequest) {
        try {

            var authResponse = authenticationService.adminRegister(registerRequest);
            return ResponseEntity.ok(authResponse);
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> clientRegister(@RequestBody ClientDto registerRequest) {
        try {

            var authResponse = authenticationService.clientRegister(registerRequest);
            return ResponseEntity.ok(authResponse);
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/register/supervisor")
    public ResponseEntity<?> supervisorRegister(@RequestBody SupervisorDto registerRequest) {
        try {

            var authResponse = authenticationService.supervisorRegister(registerRequest);
            return ResponseEntity.ok(authResponse);
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
