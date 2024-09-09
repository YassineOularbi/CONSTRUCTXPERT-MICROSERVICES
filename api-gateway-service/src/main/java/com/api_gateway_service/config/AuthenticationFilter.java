package com.api_gateway_service.config;

import com.api_gateway_service.service.JwtService;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Order(1)
public class AuthenticationFilter implements GlobalFilter, Ordered {


    private final JwtService jwtService;
    private final RouteValidator validator;
    private final AuthorizationGatewayFilterFactory authorizationGatewayFilterFactory;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (validator.getIsSecured().test(exchange.getRequest())) {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();            }

            String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).getFirst();
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    jwtService.validateToken(token);
                } catch (Exception e) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                exchange.getAttributes().put("jwtToken", token);
                return applyAuthorizationFilter(exchange, chain);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> applyAuthorizationFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        AuthorizationGatewayFilterFactory.Config config = new AuthorizationGatewayFilterFactory.Config();
        return authorizationGatewayFilterFactory
                .apply(config)
                .filter(exchange, chain);
    }
}
