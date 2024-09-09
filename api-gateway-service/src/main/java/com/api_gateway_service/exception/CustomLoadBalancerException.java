package com.api_gateway_service.exception;

public class CustomLoadBalancerException extends RuntimeException {
    public CustomLoadBalancerException(String message) {
        super(message);
    }
}
