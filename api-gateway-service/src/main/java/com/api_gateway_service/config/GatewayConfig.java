package com.api_gateway_service.config;

import com.api_gateway_service.service.JwtService;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

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
}

