package com.user_service.service;



import com.user_service.dto.*;
import com.user_service.exception.LoginException;
import com.user_service.exception.RegistrationException;
import com.user_service.mapper.UserMapper;
import com.user_service.model.Admin;
import com.user_service.model.Client;
import com.user_service.model.Supervisor;
import com.user_service.model.User;
import com.user_service.repository.AdminRepository;
import com.user_service.repository.ClientRepository;
import com.user_service.repository.SupervisorRepository;
import com.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final SupervisorRepository supervisorRepository;

    public AuthenticationResponse login(AuthenticationRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            return AuthenticationResponse.builder()
                    .token(jwtService.generateToken((User) authentication.getPrincipal()))
                    .build();
        } catch (BadCredentialsException e) {
            throw LoginException.invalidCredentials();
        } catch (UsernameNotFoundException e) {
            throw LoginException.userNotFound();
        } catch (Exception e) {
            throw LoginException.authenticationFailed();
        }
    }


    public AuthenticationResponse adminRegister(AdminDto registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RegistrationException();
        }
        var admin = (Admin) userMapper.toEntity(registerRequest);
        admin.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(adminRepository.save(admin)))
                .build();
    }

    public AuthenticationResponse clientRegister(ClientDto registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RegistrationException();
        }
        var client = (Client) userMapper.toEntity(registerRequest);
        client.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(clientRepository.save(client)))
                .build();
    }

    public AuthenticationResponse supervisorRegister(SupervisorDto registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RegistrationException();
        }
        var supervisor = (Supervisor) userMapper.toEntity(registerRequest);
        supervisor.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(supervisorRepository.save(supervisor)))
                .build();
    }

}