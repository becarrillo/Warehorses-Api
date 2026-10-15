package com.web.warehorses_secure_persistence.config;

import com.web.warehorses_secure_persistence.domain.user.util.PasswordEncoderProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderProviderConfig {

    @Bean
    public PasswordEncoderProvider provider(PasswordEncoder passwordEncoder) {
        return new PasswordEncoderProvider(passwordEncoder);
    }
}
