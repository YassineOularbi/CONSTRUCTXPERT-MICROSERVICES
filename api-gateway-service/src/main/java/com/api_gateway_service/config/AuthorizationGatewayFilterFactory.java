package com.api_gateway_service.config;

import com.api_gateway_service.enums.Role;
import com.api_gateway_service.service.JwtService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(2)
public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public AuthorizationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        System.out.println("dkheeeeel");
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            if (!isAuthorized(path, exchange, config)) {
                return Mono.error(new RuntimeException("Forbidden: Insufficient permissions"));
            }
            return chain.filter(exchange);
        };
    }

    private boolean isAuthorized(String path, ServerWebExchange exchange, Config config) {
        System.out.println("hahwaaa");
        if (path.startsWith(config.getUserServicePathPrefix())) {
            System.out.println("f useer");
            return checkUserServiceAuthorization(path, exchange, config);
        }
        if (path.startsWith(config.getTaskServicePathPrefix())) {
            return checkTaskServiceAuthorization(path, exchange, config);
        }
        if (path.startsWith(config.getResourceServicePathPrefix())) {
            return checkResourceServiceAuthorization(path, exchange, config);
        }
        if (path.startsWith(config.getProjectServicePathPrefix())) {
            return checkProjectServiceAuthorization(path, exchange, config);
        }
        return true;
    }

    private boolean checkUserServiceAuthorization(String path, ServerWebExchange exchange, Config config) {
        if (path.matches(STR."\{config.getUserServiceAdminPathPrefix()}.*")) {
            System.out.println("3nd admin");
            return userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getUserServiceSupervisorPathPrefix()}.*")) {
            System.out.println("3nd super");
            return userHasRole(exchange, config.getSupervisorRole()) || userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getUserServiceClientPathPrefix()}.*")) {
            System.out.println("3nd client");
            return userHasRole(exchange, config.getClientRole()) ||  userHasRole(exchange, config.getAdminRole());
        }
        return true;
    }

    private boolean checkTaskServiceAuthorization(String path, ServerWebExchange exchange, Config config) {
        if (path.matches(STR."\{config.getTaskServiceCreateTaskPathPrefix()}.*")) {
            return userHasRole(exchange, config.getSupervisorRole());
        }
        if (path.matches(STR."\{config.getTaskServiceGetTaskByIdPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getSupervisorRole()) || userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getTaskServiceGetTasksByProjectIdPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getSupervisorRole()) || userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getTaskServiceGetTasksIdsByProjectIdPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getSupervisorRole()) || userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getTaskServiceUpdateTaskPathPrefix()}.*")) {
            return userHasRole(exchange, config.getSupervisorRole());
        }
        if (path.matches(STR."\{config.getTaskServiceDeleteTaskPathPrefix()}.*")) {
            return userHasRole(exchange, config.getSupervisorRole());
        }
        return true;
    }

    private boolean checkResourceServiceAuthorization(String path, ServerWebExchange exchange, Config config) {
        if (path.matches(STR."\{config.getResourceServicePostPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getSupervisorRole());
        }
        if (path.matches(STR."\{config.getResourceServicePutPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getSupervisorRole());
        }
        if (path.matches(STR."\{config.getResourceServiceDeletePathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getSupervisorRole());
        }
        if (path.matches(STR."\{config.getResourceServiceGetPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getAdminRole()) || userHasRole(exchange, config.getSupervisorRole());
        }
        if (path.matches(STR."\{config.getResourceServiceGetAllPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getAdminRole()) || userHasRole(exchange, config.getSupervisorRole());
        }
        return true;
    }


    private boolean checkProjectServiceAuthorization(String path, ServerWebExchange exchange, Config config) {
        if (path.matches(STR."\{config.getProjectServicePostPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole());
        }
        if (path.matches(STR."\{config.getProjectServicePutPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole());
        }
        if (path.matches(STR."\{config.getProjectServiceDeletePathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole());
        }
        if (path.matches(STR."\{config.getProjectServiceGetPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getAdminRole()) || userHasRole(exchange, config.getSupervisorRole());
        }
        if (path.matches(STR."\{config.getProjectServiceGetAllPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getAdminRole()) || userHasRole(exchange, config.getSupervisorRole());
        }
        return true;
    }

    private boolean userHasRole(ServerWebExchange exchange, String requiredRole) {
        System.out.println("f hasRole");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            var role = jwtService.extractRole(token);
            return role.equals(requiredRole);
        }
        return false;
    }

    @Setter
    @Getter
    public static class Config {
        private String userServicePathPrefix = "/api/user/";
        private String userServiceAdminPathPrefix = "/api/user/admin/";
        private String userServiceSupervisorPathPrefix = "/api/user/supervisor/";
        private String userServiceClientPathPrefix = "/api/user/client/";

        private String taskServicePathPrefix = "/api/tasks/";
        private String taskServiceCreateTaskPathPrefix = "/api/tasks/create-task";
        private String taskServiceGetTaskByIdPathPrefix = "/api/tasks/get-task-by-id";
        private String taskServiceGetTasksByProjectIdPathPrefix = "/api/tasks/get-tasks-by-project";
        private String taskServiceGetTasksIdsByProjectIdPathPrefix = "/api/tasks/get-tasks-ids-by-project";
        private String taskServiceUpdateTaskPathPrefix = "/api/tasks/update-task";
        private String taskServiceDeleteTaskPathPrefix = "/api/tasks/delete-task";

        private String resourceServicePathPrefix = "/api/resources/";
        private String resourceServicePostPathPrefix = "/api/resources/create-resource";
        private String resourceServicePutPathPrefix = "/api/resources/update-resource";
        private String resourceServiceDeletePathPrefix = "/api/resources/delete-resource";
        private String resourceServiceGetPathPrefix = "/api/resources/get-resource-by-id";
        private String resourceServiceGetAllPathPrefix = "/api/resources/get-all-resources";

        private String projectServicePathPrefix = "/api/project/";
        private String projectServicePostPathPrefix = "/api/project/create-project";
        private String projectServicePutPathPrefix = "/api/project/update-project";
        private String projectServiceDeletePathPrefix = "/api/project/delete-project";
        private String projectServiceGetPathPrefix = "/api/project/get-project-by-id";
        private String projectServiceGetAllPathPrefix = "/api/project/get-all-projects";


        private String adminRole = Role.ADMIN.name();
        private String supervisorRole = Role.SUPERVISOR.name();
        private String clientRole = Role.CLIENT.name();
    }
}
