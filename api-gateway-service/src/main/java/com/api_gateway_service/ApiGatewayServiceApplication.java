package com.api_gateway_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication(scanBasePackages = "com.api_gateway_service")
@EnableDiscoveryClient
@EnableWebFlux
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class ApiGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayServiceApplication.class, args);
	}

	@Bean
	public DiscoveryClientRouteDefinitionLocator locator(ReactiveDiscoveryClient rd, DiscoveryLocatorProperties dl) {
		return new DiscoveryClientRouteDefinitionLocator(rd, dl);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/v3/api-docs").and().method(HttpMethod.GET).uri("lb://PROJECT-SERVICE"))
				.route(r -> r.path("/v3/api-docs").and().method(HttpMethod.GET).uri("lb://TASK-SERVICE"))
				.route(r -> r.path("/v3/api-docs").and().method(HttpMethod.GET).uri("lb://RESOURCE-SERVICE"))
				.route(r -> r.path("/v3/api-docs").and().method(HttpMethod.GET).uri("lb://USER-SERVICE"))
				.build();
	}

}
