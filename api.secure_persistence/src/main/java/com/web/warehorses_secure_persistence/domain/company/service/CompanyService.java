package com.web.warehorses_secure_persistence.domain.company.service;

import com.web.warehorses_secure_persistence.domain.company.entity.Company;
import com.web.warehorses_secure_persistence.domain.company.repository.CompanyRepository;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import com.web.warehorses_secure_persistence.exception.CompanyNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company create(Company company) {
        company.setEnabled(true);
        return companyRepository.save(company);
    }

    public Company findByNit(String id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La compañía con ese NIT no fue encontrada"));
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    /**
     * Company that owns the given user, or an error when that user id does not exist. Mirrors
     * findByNit's contract: service methods in this class throw, while the repository returns Optional.
     */
    public Company findByUser(User user) {
        return companyRepository.findByUsersContaining(user)
                .orElseThrow(() -> new CompanyNotFoundException(
                        "User (parameter) in the current query to retrieve the Company" +
                                " doesn't exist in any company yet"
                )
        );
    }

    public Optional<Company> findByEmail(String email) {
        return companyRepository.findByEmail(email);
    }

    public Optional<Company> findByBrandName(String name) {
        return companyRepository.findByName(name);
    }

    public Company update(String id, Company company) {
        Company existing = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        existing.setName(company.getName());
        existing.setEmail(company.getEmail());
        existing.setAddress(company.getAddress());
        return companyRepository.save(existing);
    }

    public void delete(String id) {
        companyRepository.deleteById(id);
    }
}