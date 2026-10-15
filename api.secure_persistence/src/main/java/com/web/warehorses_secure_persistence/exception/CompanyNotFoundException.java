package com.web.warehorses_secure_persistence.exception;

import org.springframework.http.HttpStatus;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(String message) {
        super(message);
    }
    public CompanyNotFoundException() {
        super("Exception at the query: Company not found in DB");
    }
}
