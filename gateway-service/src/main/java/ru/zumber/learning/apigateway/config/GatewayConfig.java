package ru.zumber.learning.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> userFallbackRouter() {
        return route("user-service")
                .route(path("/user/**"), http())
                .filter(lb("USER-SERVICE"))
                .filter(circuitBreaker("userServiceCircuitBreaker",
                        URI.create("forward:/fallback/user")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationFallbackRouter() {
        return route("notification-service")
                .route(path("/sendEmail"), http())
                .filter(lb("NOTIFICATION-SERVICE"))
                .filter(circuitBreaker("notificationServiceCircuitBreaker",
                        URI.create("forward:/fallback/notification")))
                .build();
    }
}
