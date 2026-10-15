package com.web.warehorses_secure_persistence.domain.client.service;

import com.web.warehorses_secure_persistence.domain.client.entity.Client;
import com.web.warehorses_secure_persistence.domain.client.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> findById(Integer id) {
        return clientRepository.findById(id);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }

    public List<Client> findByNameContainingIgnoreCase(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }

    public Client update(Integer id, Client client) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        existing.setName(client.getName());
        existing.setPhoneNumber(client.getPhoneNumber());
        return clientRepository.save(existing);
    }

    public void delete(Integer id) {
        clientRepository.deleteById(id);
    }
}