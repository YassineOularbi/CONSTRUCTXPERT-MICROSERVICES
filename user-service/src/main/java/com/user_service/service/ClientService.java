package com.user_service.service;

import com.user_service.dto.ClientDto;
import com.user_service.exception.ClientNotFoundException;
import com.user_service.mapper.UserMapper;
import com.user_service.model.Client;
import com.user_service.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserMapper clientMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
    }

    public ClientDto updateClient(Long id, ClientDto clientDto) {
        var existingClient = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        var updatedClient = (Client) clientMapper.partialUpdate(clientDto, existingClient);
        updatedClient.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        var savedClient = clientRepository.save(updatedClient);
        return (ClientDto) clientMapper.toDto(savedClient);
    }

    public void deleteClient(Long id) {
        var client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        clientRepository.delete(client);
    }
}
