package com.ecommerce.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.util.JwtUtil;
import com.ecommerce.validator.RouteValidator;

@Component
public class EcommerceFilter extends AbstractGatewayFilterFactory<EcommerceFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private RestTemplate template;

    @Autowired
    private JwtUtil jwtutil;

    public EcommerceFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing Authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    // Validate the token using JwtUtil
                    jwtutil.validateToken(authHeader);
                } catch (Exception e) {
                    System.out.println("Invalid access!...");
                    throw new RuntimeException("Unauthorized access to the application");
                }
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuration properties can be added here in the future
    }
}
