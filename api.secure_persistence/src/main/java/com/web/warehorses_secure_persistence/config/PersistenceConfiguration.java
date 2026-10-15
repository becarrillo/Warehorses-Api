package com.web.warehorses_secure_persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Explicit owner of the persistence layer.
 *
 * <p>The scan roots must be pinned: because this class declares
 * {@code @EnableJpaRepositories}, Spring Boot's auto-configured repository
 * scanning backs off completely. The packages below are therefore the ONLY
 * ones scanned -- if they drift from the real layout, every repository bean
 * silently disappears and startup fails with
 * "required a bean of type '...UserRepository' that could not be found".
 *
 * <p>Every {@code @Entity} and every repository interface lives under
 * {@code com.web.warehous_secure_persistence.domain}. Update this file
 * whenever a domain package moves.
 */
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.web.warehorses_secure_persistence.domain")
@EnableJpaRepositories(basePackages = "com.web.warehorses_secure_persistence.domain")
public class PersistenceConfiguration {
}