package com.javabruse.filtrs.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TokenValidationService {
    private final WebClient webClient;

    @Value("${auth.server.url}")
    private String authServerUrl;

    public TokenValidationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> isTokenValid(String token) {
        String url = authServerUrl + "/validate";
        return webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn(""); // Если ошибка, считаем токен невалидным
    }
}
