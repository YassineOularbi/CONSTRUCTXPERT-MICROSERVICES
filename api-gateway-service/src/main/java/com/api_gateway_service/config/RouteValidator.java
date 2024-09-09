package com.api_gateway_service.config;

import lombok.Getter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Getter
@Component
public class RouteValidator {

    private static final List<String> PUBLIC_API_ENDPOINTS_PATTERNS = List.of(
            ("/api/auth"),
            ("/swagger-ui"),
            ("/swagger-ui.html"),
            ("/v3/api-docs"),
            ("/eureka")
    );


    private final Predicate<ServerHttpRequest> isSecured =
            request -> PUBLIC_API_ENDPOINTS_PATTERNS
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
