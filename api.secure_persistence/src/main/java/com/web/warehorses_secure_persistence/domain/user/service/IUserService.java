package com.web.warehorses_secure_persistence.domain.user.service;

import com.web.warehorses_secure_persistence.domain.user.dto.UserDetailsDto;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <p>It extends {@link UserDetailsService} so JwtAuthenticationFilter,
 * JwtSecurityConfig and DaoAuthenticationProvider can inject the contract as a
 * plain Spring Security abstraction and never import api.main.
 *
 * <p>Implementations MUST return {@link UserDetailsDto} from
 * {@code loadUserByUsername}: {@code JwtProvider} casts the authentication
 * principal to that exact type when it mints the token.
 */
public interface IUserService extends UserDetailsService {

    @PostAuthorize("returnObject.idCardNumber == authentication.name" +
            " or returnObject.company.nit == principal.company.nit or hasRole('ADMIN')")
    User findById(int id);

    @PostAuthorize("returnObject.idCardNumber == authentication.name" +
            " or returnObject.company.nit == principal.company.nit or hasRole('ADMIN')")
    User findByIdCardNumber(String idCardNumber);

    @PostFilter("filterObject.company.nit == principal.company.nit or hasRole('ADMIN')")
    List<User> findAll();

    /**
     * Safe existence probe for registration; unlike findByIdCardNumber it does
     * not throw when the user is absent.
     */
    boolean existsByIdCardNumber(String idCardNumber);

    @PreAuthorize("#user.company.nit == principal.company.nit and hasRole('MANAGER')")
    User update(int id, @P("user") UserDetailsDto user);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(int id);
}