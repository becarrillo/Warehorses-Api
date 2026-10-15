package com.web.warehorses_secure_persistence.auth.util;

public interface AuthProvider {
    String generateJwt(String username, String password);
    Integer getUserId(String jwt);
    boolean logout();
}
