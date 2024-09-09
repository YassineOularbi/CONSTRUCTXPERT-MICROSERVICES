package com.api_gateway_service.config;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationGlobalFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationGlobalFilter.class);

    private final JwtService jwtService;
    private final RouteValidator validator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        logger.debug("Request Path: {}", path);
        if (validator.getIsSecured().test(exchange.getRequest())) {
            logger.debug("Path is secured, performing JWT validation.");
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                logger.error("Missing Authorization header.");
                return Mono.error(new RuntimeException("Missing authorization header"));
            }

            String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            } else {
                logger.error("Invalid Authorization header format.");
                return Mono.error(new RuntimeException("Invalid authorization header format"));
            }

            try {
                jwtService.validateToken(authHeader);
                logger.debug("JWT token validated successfully.");
            } catch (Exception e) {
                logger.error("JWT validation failed: {}", e.getMessage());
                return Mono.error(new RuntimeException("Unauthorized access"));
            }
        }

        return chain.filter(exchange);
    }
}
