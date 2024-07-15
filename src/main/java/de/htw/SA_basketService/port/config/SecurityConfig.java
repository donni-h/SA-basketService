package de.htw.SA_basketService.port.config;

import de.htw.SA_basketService.port.security.KeycloakJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()))
                );
        return http.build();
    }

}
