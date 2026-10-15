package com.web.warehorses_secure_persistence.jwt.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Writes and clears the JWT cookie.
 *
 * <p>This class is deliberately STATELESS. It used to keep the cookies in an
 * in-memory Map keyed by path, which was wrong: the browser already owns the
 * cookie, so the server keeping a copy only created bugs -- a shared entry for
 * every user, a null key written on logout, and setHeader/addHeader being mixed
 * between the first and subsequent logins.
 */
@Component
public class JwtCookieProvider {

    public void sendJwtCookie(String value, HttpServletResponse servletResponse) {
        writeCookie(value, Duration.ofSeconds(12 * 3600), servletResponse);
    }

    public void removeJwtCookie(HttpServletResponse servletResponse) {
        // maxAge zero makes the browser delete it. The value must be empty, never null.
        writeCookie("", Duration.ZERO, servletResponse);
    }

    private void writeCookie(String value, Duration maxAge, HttpServletResponse servletResponse) {
        final ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("jwt")
                .httpOnly(true)
                .sameSite("Lax")
                .secure(false)
                .path("/")
                .value(value)
                .maxAge(maxAge);
        // setHeader would replace any Set-Cookie already written for this response.
        servletResponse.setHeader("Set-Cookie", cookieBuilder.build().toString());
    }
}
