package com.web.warehorses_api_main.socket.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketConfig {

    @Value("${socket.config.host}")
    private String host;

    @Value("${socket.config.port}")
    private Integer port;

    @Value("${socket.config.allowed-headers}")
    private String allowedHeaders;

    @Value("${app.cors.allowed-origin}")
    private String allowedOrigin;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config;
        config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);

        // CORS: allowed origin(s) from the SPA (localhost:5173)
        config.setOrigin(allowedOrigin);

        // Allowed REQUEST headers the browser may send (NOT response headers).
        // "Authorization" lets the client pass its JWT; "*" keeps it simple for dev.
        config.setAllowHeaders(allowedHeaders);
        config.setAllowCustomRequests(true);

        // Validate the JWT sent by the client during the handshake.
        // The token can arrive as a query param (?token=...) or an Authorization header.
        /*config.setAuthorizationListener(handshakeData -> {
            String token = extractToken(handshakeData);
            if (token == null || !jwtUtil.validateJwtToken(token)) {
                throw new RuntimeException("Unauthorized: invalid or missing JWT");
            }
            return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
        });*/
        return new SocketIOServer(config);
    }
}