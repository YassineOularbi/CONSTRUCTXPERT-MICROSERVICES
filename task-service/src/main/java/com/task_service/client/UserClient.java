package com.task_service.client;

import com.task_service.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/user/")
public interface UserClient {

    @GetMapping("/get-user-by-username/{username}")
    User getUserByUsername(@PathVariable("username") String username);
}
