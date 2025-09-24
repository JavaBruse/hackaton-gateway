package org.Trochilidae.filtrs;

import org.Trochilidae.filtrs.utils.TokenValidationService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final TokenValidationService tokenValidationService;

    public JwtAuthFilter(TokenValidationService tokenValidationService) {
        super(Config.class);
        this.tokenValidationService = tokenValidationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "FORBIDDEN", HttpStatus.FORBIDDEN);
            }
            return tokenValidationService.isTokenValid(authHeader)
                    .flatMap(isValid -> {
                        if (isValid.length() > 1) {

                            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                    .header("X-User-Id", isValid)
                                    .build();
                            return chain.filter(exchange.mutate().request(modifiedRequest).build());

                        } else {
                            // Если токен невалиден, возвращаем ошибку
                            return onError(exchange, "FORBIDDEN", HttpStatus.FORBIDDEN);
                        }
                    });
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // Конфигурация фильтра, если требуется
    }

}
