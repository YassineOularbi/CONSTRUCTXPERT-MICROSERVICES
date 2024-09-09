package com.task_service.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String jwtToken = RequestContext.getJwtToken();
        if (jwtToken != null) {
            template.header(HttpHeaders.AUTHORIZATION, STR."Bearer \{jwtToken}");
        }
    }
}

