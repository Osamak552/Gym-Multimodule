package com.epam.filter;

import com.epam.exception.UnauthorizedException;

import com.epam.feign.WebFluxAuthenticationProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.Objects;


@Component
@Slf4j

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private  RouteValidator validator;
    @Autowired
    private WebFluxAuthenticationProxy webFluxAuthenticationProxy;
    @Autowired
    private  RestTemplate restTemplate;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new UnauthorizedException("Missing authorization header");
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                return validateToken(authHeader)
                        .flatMap(valid -> {
                            if (valid) {
                                return chain.filter(exchange);
                            } else {
                                return Mono.error(new UnauthorizedException("Invalid token"));
                            }
                        });

            }

            return chain.filter(exchange);
        };
    }


    private Mono<Boolean> validateToken(String token)  {
        return webFluxAuthenticationProxy.validateToken(token)
                    .map(response -> {
                        System.out.println("In Response: " + response);
                        return response.equals("valid");});
    }
    public static class Config {
        // Empty class for configuration if needed
    }
}
