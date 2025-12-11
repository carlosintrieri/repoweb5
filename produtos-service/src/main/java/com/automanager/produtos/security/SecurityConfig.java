package com.automanager.produtos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    new AntPathRequestMatcher("/h2-console/**"),
                    new AntPathRequestMatcher("/api/**/teste"),
                    new AntPathRequestMatcher("/api/**/count")
                ).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/**", HttpMethod.GET.name())).hasAnyRole("ADMIN", "USER")
                .requestMatchers(new AntPathRequestMatcher("/api/**", HttpMethod.POST.name())).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/**", HttpMethod.PUT.name())).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/**", HttpMethod.PATCH.name())).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/**", HttpMethod.DELETE.name())).hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }
}
