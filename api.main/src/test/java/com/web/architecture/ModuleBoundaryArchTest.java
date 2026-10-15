package com.web.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Enforces the boundary between the two Gradle modules, replacing module-info.java.
 *
 * <p>It cannot be module-info.java. A Spring Boot fat jar stores its dependencies
 * as nested jars under BOOT-INF/lib, and the JVM module path cannot reference
 * nested jars, so a named module is never resolvable at runtime -- hence
 * "module api.main was not found". The same guarantee is asserted here instead,
 * where it costs one test run.
 *
 * <p>This test lives in api.main because that is the only module with both
 * modules on its test classpath, so ArchUnit can import every class.
 *
 * <p>Layout being guarded:
 * <pre>
 *   api.main                 ->  com.web.warehous_api_main..            (controllers, sockets)
 *   api.secure_persistence   ->  com.web.warehorses_secure_persistence..
 *                                 ..auth..  ..jwt..  ..domain..  ..config..
 * </pre>
 */
class ModuleBoundaryArchTest {

    /** The lower module: auth, JWT and the whole domain. */
    private static final String LIBRARY_MODULE = "com.web.warehorses_secure_persistence..";
    /** The front module: REST controllers and the Socket.IO layer. */
    private static final String FRONT_MODULE = "com.web.warehous_api_main..";
    /** Domain: entities, repositories, services, mappers. */
    private static final String DOMAIN = "com.web.warehorses_secure_persistence.domain..";
    /** JWT plumbing. */
    private static final String JWT = "com.web.warehorses_secure_persistence.jwt..";

    private static JavaClasses productionClasses;

    @BeforeAll
    static void importClasses() {
        productionClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.web");
    }

    @Test
    @DisplayName("the dependency edge must stay one-way")
    void lowerModuleDoesNotDependOnFrontModule() {
        noClasses()
                .that().resideInAPackage(LIBRARY_MODULE)
                .should().dependOnClassesThat().resideInAPackage(FRONT_MODULE)
                .because("api.secure_persistence is the lower module; depending "
                        + "back on api.main would make the edge circular and "
                        + "the fat jar unable to resolve the modules")
                .check(productionClasses);
    }

    @Test
    @DisplayName("the domain must not depend on the JWT plumbing")
    void domainDoesNotDependOnJwt() {
        noClasses()
                .that().resideInAPackage(DOMAIN)
                .should().dependOnClassesThat().resideInAPackage(JWT)
                .because("tokens are a delivery concern; the domain should be "
                        + "usable without them (tests, batch jobs, other front ends)")
                .check(productionClasses);
    }

    @Test
    @DisplayName("entities must not depend on services")
    void domainEntitiesDoNotDependOnServices() {
        noClasses()
                .that().resideInAnyPackage(
                        "com.web.warehorses_secure_persistence.domain..entity..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.web.warehorses_secure_persistence.domain..service..")
                .because("an entity depending on a service inverts the layering "
                        + "and makes the model impossible to test in isolation")
                .check(productionClasses);
    }
}