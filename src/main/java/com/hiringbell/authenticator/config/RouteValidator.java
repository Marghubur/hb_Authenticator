package com.hiringbell.authenticator.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/authenticate", "/registration", "/oauth"
    );

    public Predicate<HttpServletRequest> isSecured =
            request -> openApiEndpoints
                        .stream()
                        .noneMatch(uri -> request.getRequestURI().contains(uri));
}
