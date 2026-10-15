package com.web.warehorses_api_main.controller;

import com.web.warehorses_secure_persistence.auth.dto.LoginResponseDto;
import com.web.warehorses_secure_persistence.auth.dto.LoginRequestDto;
import com.web.warehorses_secure_persistence.auth.util.AuthProvider;
import com.web.warehorses_secure_persistence.domain.company.dto.CompanyResponseDto;
import com.web.warehorses_secure_persistence.domain.company.service.CompanyService;
import com.web.warehorses_secure_persistence.domain.company.util.mapper.CompanyMapper;
import com.web.warehorses_secure_persistence.domain.user.service.UserService;
import com.web.warehorses_secure_persistence.jwt.util.JwtCookieProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;


@RestController
@RequestMapping("/v1/auth")
public class AuthV1Controller {

    private final AuthProvider authProvider;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final UserService userService;
    private final JwtCookieProvider jwtCookieProvider;

    private String jwt;

    public AuthV1Controller(AuthProvider authProviderImpl, CompanyService companyService, CompanyMapper companyMapper, UserService userService, JwtCookieProvider jwtCookieProvider) {
        this.authProvider = authProviderImpl;
        this.companyService = companyService;
        this.companyMapper = companyMapper;
        this.userService = userService;
        this.jwtCookieProvider = jwtCookieProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto requestDto,
            HttpServletResponse servletResponse) {

        CompanyResponseDto companyResponseDto;
        int userId;
        try {
            jwt = authProvider.generateJwt(requestDto.username(), requestDto.password());
            jwtCookieProvider.sendJwtCookie(jwt, servletResponse);
            userId = authProvider.getUserId(jwt);
            companyResponseDto = companyMapper.toDto(
                    companyService.findByUser(userService.findById(userId)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(
                LoginResponseDto.builder()
                        .userId(userId)
                        .userCompanyNit(companyResponseDto.nit())
                        .userCompanyName(companyResponseDto.name())
                        .userCompanyEmail(companyResponseDto.email())
                        .timestamp(LocalDateTime.now(ZoneId.of("GMT-5")))
                        .build(),
                HttpStatusCode.valueOf(201));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<java.lang.Boolean> logout(HttpServletResponse servletResponse) {
        try {
            final boolean isLoggedOut = authProvider.logout();
            if (isLoggedOut) {
                jwtCookieProvider.removeJwtCookie(servletResponse);
            }
            return ResponseEntity.ok(isLoggedOut);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
