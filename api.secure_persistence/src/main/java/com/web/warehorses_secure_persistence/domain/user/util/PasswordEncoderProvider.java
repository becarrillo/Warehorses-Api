package com.web.warehorses_secure_persistence.domain.user.util;

import org.springframework.security.crypto.password.PasswordEncoder;


public class PasswordEncoderProvider {
    private static PasswordEncoder passwordEncoder;

    public PasswordEncoderProvider(PasswordEncoder passwordEncoder) {
        PasswordEncoderProvider.passwordEncoder = passwordEncoder;
    }

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
