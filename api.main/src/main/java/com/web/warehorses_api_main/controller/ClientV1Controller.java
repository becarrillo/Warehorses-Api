package com.web.warehorses_api_main.controller;

import com.web.warehorses_secure_persistence.domain.client.dto.ClientResponseDto;
import com.web.warehorses_secure_persistence.domain.client.entity.Client;
import com.web.warehorses_secure_persistence.domain.client.service.ClientService;
import com.web.warehorses_secure_persistence.domain.client.util.mapper.ClientMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/clients")
public class ClientV1Controller {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ClientV1Controller(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> listAllClients() {
        final List<Client> allClients = clientService.findAll();
        return ResponseEntity.ok(
                allClients
                        .stream()
                        .map(clientMapper::toDto)
                        .toList()
        );
    }
}
