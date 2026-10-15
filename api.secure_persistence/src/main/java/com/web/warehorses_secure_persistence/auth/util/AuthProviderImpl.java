package com.web.warehorses_secure_persistence.auth.util;

import com.web.warehorses_secure_persistence.jwt.util.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthProviderImpl implements AuthProvider {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private Authentication authentication;

    public AuthProviderImpl(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String generateJwt(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                password);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return jwtProvider.generateJwtFromAuthentication(
                authenticate(usernamePasswordAuthenticationToken)
        );
    }

    private Authentication authenticate(
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
    ) {
        authentication = authenticationManager.authenticate(
                usernamePasswordAuthenticationToken
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public Integer getUserId(String jwt) {
        return jwtProvider.getClaims(jwt).get("userId", Integer.class);
    }

    @Override
    public boolean logout() {
        authentication.setAuthenticated(false);
        authentication = null;
        SecurityContextHolder.clearContext();
        return true;
    }
}
