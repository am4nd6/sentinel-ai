package com.sentinelai.common.security;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/health", "/actuator/health", "/v3/api-docs").permitAll().anyRequest().authenticated())
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .logout(logout -> logout.logoutUrl("/api/v1/auth/logout").clearAuthentication(true));
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(
            @Value("${sentinel.security.dev-secret:sentinel-local-development-secret-32-chars}") String secret,
            @Value("${sentinel.security.issuer:}") String issuer) {
        if (!issuer.isBlank()) return org.springframework.security.oauth2.jwt.JwtDecoders.fromIssuerLocation(issuer);
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        Converter<Jwt, java.util.Collection<GrantedAuthority>> rolesConverter = jwt -> {
            Object roles = jwt.getClaims().get("roles");
            if (!(roles instanceof Iterable<?> iterable)) return java.util.List.of();
            return java.util.stream.StreamSupport.stream(iterable.spliterator(), false)
                    .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role))
                    .map(authority -> (GrantedAuthority) authority).toList();
        };
        converter.setJwtGrantedAuthoritiesConverter(rolesConverter);
        return converter;
    }
}
