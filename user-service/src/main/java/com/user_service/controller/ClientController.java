package com.user_service.controller;

import com.user_service.dto.ClientDto;
import com.user_service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/get-all-clients")
    public ResponseEntity<?> getAllClients() {
        try {
            var clients = clientService.getAllClients();
            return ResponseEntity.ok(clients);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-client-by-id/{id}")
    public ResponseEntity<?> getClientById(@PathVariable("id") String id) {
        try {
            var client = clientService.getClientById(Long.valueOf(id));
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update-client/{id}")
    public ResponseEntity<?> updateClient(@PathVariable("id") String id, @RequestBody ClientDto clientDto) {
        try {
            var updatedClient = clientService.updateClient(Long.valueOf(id), clientDto);
            return ResponseEntity.ok(updatedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-client/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") String id) {
        try {
            clientService.deleteClient(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
