package com.web.warehorses_api_main.controller;

import com.web.warehorses_secure_persistence.domain.company.dto.CompanyResponseDto;
import com.web.warehorses_secure_persistence.domain.company.entity.Company;
import com.web.warehorses_secure_persistence.domain.company.service.CompanyService;
import com.web.warehorses_secure_persistence.domain.company.util.mapper.CompanyMapper;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import com.web.warehorses_secure_persistence.domain.user.service.UserService;
import com.web.warehorses_secure_persistence.domain.warehouse.util.mapper.WarehouseMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/companies")
public class CompanyV1Controller {
    private final CompanyService companyService;
    private final UserService userService;
    private final CompanyMapper companyMapper;

    public CompanyV1Controller(CompanyService companyService, UserService userService, CompanyMapper companyMapper, WarehouseMapper warehouseMapper) {
        this.companyService = companyService;
        this.userService = userService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public ResponseEntity<List<Company>> listAllCompanies() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping(path = "/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompanyByNit(
            @PathVariable("companyId") String nit) {
        try {
            final Company company = companyService.findByNit(nit);

            return new ResponseEntity<>(
                    companyMapper.toDto(company),
                    HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/company/users/{userId}")
    public ResponseEntity<CompanyResponseDto> getCompanyByUserId(@PathVariable int userId) {
        try {
            final User user = userService.findById(userId);
            return ResponseEntity.ok(companyMapper.toDto(companyService.findByUser(user)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
