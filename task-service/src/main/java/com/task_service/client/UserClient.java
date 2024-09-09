package com.task_service.client;

import com.task_service.config.FeignClientConfiguration;
import com.task_service.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/user/", configuration = FeignClientConfiguration.class)
public interface UserClient {

    @GetMapping("/get-user-by-username/{username}")
    Optional<User> getUserByUsername(@PathVariable("username") String username);
}
