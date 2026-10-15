package com.web.warehorses_secure_persistence.domain.company.repository;

import com.web.warehorses_secure_persistence.domain.company.entity.Company;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    Optional<Company> findByEmail(String email);

    Optional<Company> findByName(String name);

    Optional<Company> findByUsersContaining(User user);

    Optional<Company> findByNit(String nit);
}