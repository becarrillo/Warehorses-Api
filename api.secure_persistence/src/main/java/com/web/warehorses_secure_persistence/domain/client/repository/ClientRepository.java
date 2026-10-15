package com.web.warehorses_secure_persistence.domain.client.repository;

import com.web.warehorses_secure_persistence.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByPhoneNumber(String phoneNumber);

    List<Client> findByNameContainingIgnoreCase(String name);
}