package com.gudlad.rest.webservices.restfulwebservices.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1) all requests are authenticated
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        // 2) if a request is not authenticated, a web page is shown
        http.httpBasic(Customizer.withDefaults());
        // 3) disable csrf-> post,put
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}