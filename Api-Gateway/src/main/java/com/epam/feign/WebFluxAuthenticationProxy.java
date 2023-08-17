package com.epam.feign;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
@Scope("prototype")
public class WebFluxAuthenticationProxy {

    private final WebClient.Builder webClientBuilder;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public WebFluxAuthenticationProxy(WebClient.Builder webClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.webClientBuilder = webClientBuilder;
        this.lbFunction = lbFunction;
    }

    public Mono<String> validateToken(String token) {
        return webClientBuilder
                .build()
                .get()
                .uri("http://Security-Service/auth/validate?token="+token)
                .retrieve()
                .bodyToMono(String.class)
                .subscribeOn(Schedulers.boundedElastic());

    }
}
