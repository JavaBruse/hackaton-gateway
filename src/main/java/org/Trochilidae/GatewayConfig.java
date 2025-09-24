package org.Trochilidae;

import org.Trochilidae.filtrs.CookieForwardFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final CookieForwardFilter cookieForwardFilter;

    public GatewayConfig(CookieForwardFilter cookieForwardFilter) {
        this.cookieForwardFilter = cookieForwardFilter;
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("security", r -> r.path("/security/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(cookieForwardFilter))
                        .uri("http://security:8189/"))
                .build();
    }
}

