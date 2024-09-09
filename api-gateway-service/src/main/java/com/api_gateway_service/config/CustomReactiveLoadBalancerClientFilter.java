package com.api_gateway_service.config;

import java.net.URI;
import java.util.Set;

import com.api_gateway_service.exception.CustomLoadBalancerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import reactor.core.publisher.Mono;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

public class CustomReactiveLoadBalancerClientFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(CustomReactiveLoadBalancerClientFilter.class);

    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;

    private final LoadBalancerClientFactory clientFactory;
    private final GatewayLoadBalancerProperties properties;

    public CustomReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory,
                                                  GatewayLoadBalancerProperties properties) {
        this.clientFactory = clientFactory;
        this.properties = properties;
    }

    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);

        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }

        addOriginalRequestUrl(exchange, url);

        String serviceId = url.getHost();

        DefaultRequest<RequestDataContext> lbRequest = new DefaultRequest<>(new RequestDataContext(
                new RequestData(exchange.getRequest(), exchange.getAttributes()), getHint(serviceId)));

        return choose(lbRequest, serviceId).doOnNext(response -> {
            if (!response.hasServer()) {
                throw new CustomLoadBalancerException(STR."No instance available for \{serviceId}");
            }

            ServiceInstance instance = response.getServer();
            URI requestUrl = reconstructURI(instance, exchange.getRequest().getURI());

            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
        }).then(chain.filter(exchange));
    }

    private Mono<Response<ServiceInstance>> choose(Request<RequestDataContext> lbRequest, String serviceId) {
        ReactorLoadBalancer<ServiceInstance> loadBalancer = clientFactory.getInstance(serviceId,
                ReactorServiceInstanceLoadBalancer.class);
        if (loadBalancer == null) {
            throw new CustomLoadBalancerException(STR."No load balancer available for \{serviceId}");
        }
        return loadBalancer.choose(lbRequest);
    }

    private URI reconstructURI(ServiceInstance instance, URI originalUri) {
        return LoadBalancerUriTools.reconstructURI(instance, originalUri);
    }

    private String getHint(String serviceId) {
        return "default";
    }
}
