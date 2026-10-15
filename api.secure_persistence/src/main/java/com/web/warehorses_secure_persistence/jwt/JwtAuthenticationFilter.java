package com.web.warehorses_secure_persistence.jwt;

import com.web.warehorses_secure_persistence.jwt.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtProvider jwtProvider;
    // Spring's UserDetailsService, not any api.main type: this module must not
    // depend on the domain. loadUserByUsername() is declared here, and exactly
    // one bean implements it (api.main's UserService).
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest servletRequest,
            @NonNull HttpServletResponse servletResponse,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = servletRequest.getServletPath();
        if (path.equals("/v1/auth/login") || path.startsWith("/socket.io/")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            try {
                String jwt = getJwtFromCookie(servletRequest);
                if (jwt != null && jwtProvider.validateToken(jwt)) {
                    String username = jwtProvider.getUsernameFromJwt(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(servletRequest)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                logger.error("Cannot set user authentication: {}", e.getMessage());
                logger.debug("Authentication failed with an invalid or expired token", e);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Value("${app.jwt.cookie.name:jwt}")
    private String cookieName;

    private String getJwtFromCookie(HttpServletRequest servletRequest) {
        Cookie jwtCookie = WebUtils.getCookie(servletRequest, cookieName);
        if (jwtCookie == null) {
            // Diagnostic: if this shows up on every protected request the browser
            // is not sending the cookie at all (wrong path/same-site, or the
            // client called fetch without credentials: 'include').
            logger.debug("No '{}' cookie on {} -- request is unauthenticated",
                    cookieName, servletRequest.getRequestURI());
            return null;
        }
        String value = jwtCookie.getValue();
        return value == null || value.isBlank() ? null : value;
    }
}