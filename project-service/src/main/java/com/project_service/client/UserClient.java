package com.project_service.client;

import com.project_service.config.FeignClientConfiguration;
import com.project_service.model.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/user/", configuration = FeignClientConfiguration.class)
public interface UserClient {

    @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "fallbackGetUserByUsername")
    @GetMapping("/get-user-by-username/{username}")
    Optional<User> getUserByUsername(@PathVariable("username") String username);

    Logger logger = LoggerFactory.getLogger(UserClient.class);

    default String fallbackGetUserByUsername(String username, Throwable throwable) {
        logger.error("Failed to fetch user by username: {} due to: {}", username, throwable.getMessage());
        return "Service is temporarily unavailable. Please try again later.";
    }
}
