package com.api_gateway_service.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

//    @Bean
//    @Order(1)
//    public GlobalFilter authenticationFilter(JwtService jwtService, RouteValidator validator) {
//        return new AuthenticationFilter(jwtService, validator);
//    }
//
//    @Bean
//    @Order(2)
//    public GatewayFilter authorizationFilter(AuthorizationGatewayFilterFactory authorizationGatewayFilterFactory) {
//        return authorizationGatewayFilterFactory.apply(new AuthorizationGatewayFilterFactory.Config());
//    }

    @Bean
    public GlobalFilter customReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory,
                                                               GatewayLoadBalancerProperties properties) {
        return new CustomReactiveLoadBalancerClientFilter(clientFactory, properties);
    }

    @Bean
    public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(
            CircuitBreakerRegistry circuitBreakerRegistry, TimeLimiterRegistry timeLimiterRegistry,
            Resilience4JConfigurationProperties resilience4JConfigurationProperties) {
        return new ReactiveResilience4JCircuitBreakerFactory(circuitBreakerRegistry, timeLimiterRegistry, resilience4JConfigurationProperties);
    }
}

