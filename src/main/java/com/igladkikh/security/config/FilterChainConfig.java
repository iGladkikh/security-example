package com.igladkikh.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class FilterChainConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/", "/index.html", "/hello", "/json").permitAll()
                                .requestMatchers("/admin.**").hasRole("ADMIN")
                                .requestMatchers("/user.html").hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .build();
    }
}
