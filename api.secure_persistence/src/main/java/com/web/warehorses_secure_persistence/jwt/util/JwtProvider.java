package com.web.warehorses_secure_persistence.jwt.util;

import com.web.warehorses_secure_persistence.domain.user.entity.User;
import com.web.warehorses_secure_persistence.domain.user.repository.UserRepository;
import com.web.warehorses_secure_persistence.exception.UserNotFoundException;
import com.web.warehorses_secure_persistence.jwt.JwtCustomClaimsProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private final UserRepository userRepository;

    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateJwtFromAuthentication(Authentication authentication) {
        org.springframework.security.core.userdetails.User userPrincipal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        final User user = userRepository.findByIdCardNumber(userPrincipal.getUsername())
                .orElseThrow(UserNotFoundException::new);
        final JwtCustomClaimsProvider applicationJwtClaimsProvider =
                (userDetails) -> Map.of(
                        "userId",
                        userDetails.getUserId(),
                        "authorities",
                        userPrincipal.getAuthorities(),
                        "role",
                        userDetails.getRole().name(),
                        "companyNit",
                        userDetails.getCompany().getNit(),
                        "isAccountNonLocked",
                        Boolean.toString(userPrincipal.isAccountNonLocked()));
        return constructTokenFromUsernameAndClaims(
                userPrincipal.getUsername(),    // is idCardNumber
                applicationJwtClaimsProvider.extractFromCurrentUserDetails(user)
        );
    }

    private String constructTokenFromUsernameAndClaims(String username, Map<String,Object> claims) {
        // NOTE: claims() must come BEFORE subject()/issuedAt()/expiration():
        // claims(Map) replaces the whole payload, wiping anything set before it.
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        if (authToken == null || authToken.isBlank()) {
            return false;
        }
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }
        return false;
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
