package com.floo.api_gateway.config;
// This configuration is for Spring Cloud Gateway.
// Place this in your Spring Cloud Gateway's configuration.

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.util.AntPathMatcher;


@Configuration
public class CorsGlobalConfiguration {

    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Content-Length, Authorization, credential, X-XSRF-TOKEN";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS, PATCH";
    private static final String ALLOWED_ORIGIN = "http://localhost:3000"; // Your Next.js frontend
    private static final String MAX_AGE = "3600"; // 1 hour

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ALLOWED_ORIGIN);
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHODS);
                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ALLOWED_HEADERS);
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"); // If you use credentials

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

    // Alternative using CorsConfiguration (often preferred and more standard)
    // You can use this bean instead of the WebFilter above.
    // @Bean
    // public org.springframework.web.cors.reactive.CorsWebFilter corsWebFilterGlobal() {
    //     CorsConfiguration corsConfig = new CorsConfiguration();
    //     corsConfig.setAllowedOrigins(Collections.singletonList(ALLOWED_ORIGIN));
    //     corsConfig.setMaxAge(Long.parseLong(MAX_AGE));
    //     corsConfig.setAllowedMethods(Arrays.asList(ALLOWED_METHODS.split(",")));
    //     corsConfig.setAllowedHeaders(Arrays.asList(ALLOWED_HEADERS.split(",")));
    //     corsConfig.setAllowCredentials(true);

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new AntPathMatcher());
    //     // Apply this CORS configuration to all paths.
    //     // You can be more specific with path patterns if needed, e.g., "/api/**"
    //     source.registerCorsConfiguration("/**", corsConfig);

    //     return new org.springframework.web.cors.reactive.CorsWebFilter(source);
    // }
}
