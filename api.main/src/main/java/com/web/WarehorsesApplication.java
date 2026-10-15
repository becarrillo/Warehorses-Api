package com.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Single entry point of the whole application. Everything lives in one module,
// so this is the only @SpringBootApplication and the default component scan
// (package com.web) already covers com.web.auth, com.web.jwt and
// com.web.warehorses_api.
@SpringBootApplication
public class WarehorsesApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehorsesApplication.class, args);
    }
}