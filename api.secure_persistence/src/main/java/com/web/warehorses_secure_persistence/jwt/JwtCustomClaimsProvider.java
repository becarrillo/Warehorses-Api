package com.web.warehorses_secure_persistence.jwt;

import com.web.warehorses_secure_persistence.domain.user.dto.UserDetailsDto;
import com.web.warehorses_secure_persistence.domain.user.entity.User;

import java.util.Map;

public interface JwtCustomClaimsProvider {

    Map<String,Object> extractFromCurrentUserDetails(User user);
}
