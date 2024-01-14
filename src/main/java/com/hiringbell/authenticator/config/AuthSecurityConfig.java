package com.hiringbell.authenticator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class AuthSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/", "/login**").permitAll()
                                .anyExchange().authenticated()
                )
                .oauth2Client(Customizer.withDefaults()) // Use withDefaults for default configuration
                .build();
    }
}