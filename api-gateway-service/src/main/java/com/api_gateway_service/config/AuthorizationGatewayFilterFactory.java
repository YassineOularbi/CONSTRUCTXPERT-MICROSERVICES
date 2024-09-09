package com.api_gateway_service.config;

import com.api_gateway_service.enums.Role;
import com.api_gateway_service.service.JwtService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(2)
public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> implements Ordered {

    private final JwtService jwtService;

    public AuthorizationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }


    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            if (!isAuthorized(path, exchange, config)) {
                return Mono.error(new RuntimeException("Forbidden: Insufficient permissions"));
            }
            return chain.filter(exchange);
        };
    }

    private boolean isAuthorized(String path, ServerWebExchange exchange, Config config) {
        if (path.startsWith(config.getUserServicePathPrefix())) {
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
        return false;
    }

    private boolean checkUserServiceAuthorization(String path, ServerWebExchange exchange, Config config) {
        if (path.matches(STR."\{config.getUserServiceGetAllUserPathPrefix()}.*")) {
            return userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getUserServiceGetUserByIdPathPrefix()}.*")) {
            return userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getUserServiceGetUserByUsernamePathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) || userHasRole(exchange, config.getSupervisorRole()) || userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getUserServiceAdminPathPrefix()}.*")) {
            return userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getUserServiceSupervisorPathPrefix()}.*")) {
            return userHasRole(exchange, config.getSupervisorRole()) || userHasRole(exchange, config.getAdminRole());
        }
        if (path.matches(STR."\{config.getUserServiceClientPathPrefix()}.*")) {
            return userHasRole(exchange, config.getClientRole()) ||  userHasRole(exchange, config.getAdminRole());
        }
        return false;
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
        return false;
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
        return false;
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
        return false;
    }

    private boolean userHasRole(ServerWebExchange exchange, String requiredRole) {
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
        private String userServicePathPrefix = "/USER-SERVICE/api/";
        private String userServiceGetAllUserPathPrefix = "/USER-SERVICE/api/user/get-all-users";
        private String userServiceGetUserByIdPathPrefix = "/USER-SERVICE/api/user/get-user-by-id";
        private String userServiceGetUserByUsernamePathPrefix = "/USER-SERVICE/api/user/get-user-by-id";
        private String userServiceAdminPathPrefix = "/USER-SERVICE/api/admin/";
        private String userServiceSupervisorPathPrefix = "/USER-SERVICE/api/supervisor/";
        private String userServiceClientPathPrefix = "/USER-SERVICE/api/client/";

        private String taskServicePathPrefix = "/TASK-SERVICE/api/tasks/";
        private String taskServiceCreateTaskPathPrefix = "/TASK-SERVICE/api/tasks/create-task";
        private String taskServiceGetTaskByIdPathPrefix = "/TASK-SERVICE/api/tasks/get-task-by-id";
        private String taskServiceGetTasksByProjectIdPathPrefix = "/TASK-SERVICE/api/tasks/get-tasks-by-project";
        private String taskServiceGetTasksIdsByProjectIdPathPrefix = "/TASK-SERVICE/api/tasks/get-tasks-ids-by-project";
        private String taskServiceUpdateTaskPathPrefix = "/TASK-SERVICE/api/tasks/update-task";
        private String taskServiceDeleteTaskPathPrefix = "/TASK-SERVICE/api/tasks/delete-task";

        private String resourceServicePathPrefix = "/RESOURCE-SERVICE/api/resources/";
        private String resourceServicePostPathPrefix = "/RESOURCE-SERVICE/api/resources/create-resource";
        private String resourceServicePutPathPrefix = "/RESOURCE-SERVICE/api/resources/update-resource";
        private String resourceServiceDeletePathPrefix = "/RESOURCE-SERVICE/api/resources/delete-resource";
        private String resourceServiceGetPathPrefix = "/RESOURCE-SERVICE/api/resources/get-resource-by-id";
        private String resourceServiceGetAllPathPrefix = "/RESOURCE-SERVICE/api/resources/get-all-resources";

        private String projectServicePathPrefix = "/PROJECT-SERVICE/api/project/";
        private String projectServicePostPathPrefix = "/PROJECT-SERVICE/api/project/create-project";
        private String projectServicePutPathPrefix = "/PROJECT-SERVICE/api/project/update-project";
        private String projectServiceDeletePathPrefix = "/PROJECT-SERVICE/api/project/delete-project";
        private String projectServiceGetPathPrefix = "/PROJECT-SERVICE/api/project/get-project-by-id";
        private String projectServiceGetAllPathPrefix = "/PROJECT-SERVICE/api/project/get-all-projects";


        private String adminRole = Role.ADMIN.name();
        private String supervisorRole = Role.SUPERVISOR.name();
        private String clientRole = Role.CLIENT.name();
    }
}
