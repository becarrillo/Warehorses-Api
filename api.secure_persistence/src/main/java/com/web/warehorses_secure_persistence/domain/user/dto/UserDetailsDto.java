package com.web.warehorses_secure_persistence.domain.user.dto;

import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityResponseDto;
import com.web.warehorses_secure_persistence.domain.company.dto.CompanyDetailsDto;
import com.web.warehorses_secure_persistence.domain.company.dto.CompanyResponseDto;
import com.web.warehorses_secure_persistence.domain.company.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailsDto implements UserDetails {
    private int userId;
    private String idCardNumber;
    private String password;
    private String fullname;
    private boolean isEnabled;
    private UserRole role;
    private CompanyDetailsDto company;
    private List<ActivityResponseDto> activities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Empty authorities silently deny every hasRole()/hasAuthority() check
        // (including the @PostAuthorize rules on UserService), so derive
        // the role authority here following the ROLE_ convention.
        return role == null ? List.of()
                : List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return idCardNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return role != null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled && company.isEnabled();
    }

    public UserDetailsDto(int userId,
                          String idCardNumber,
                          String fullname,
                          UserRole role,
                          boolean isEnabled,
                          CompanyDetailsDto company,
                          List<ActivityResponseDto> activities) {
        this.userId = userId;
        this.idCardNumber=idCardNumber;
        this.fullname=fullname;
        this.role = role;
        this.isEnabled = isEnabled;
        this.company = company;
        this.activities = activities;
    }

    public enum UserRole {
        ADMIN("ADMIN"),
        MANAGER("MANAGER"),
        USER("USER");

        final String name;

        UserRole(final String name) {
            this.name = name;
        }
    }
}
