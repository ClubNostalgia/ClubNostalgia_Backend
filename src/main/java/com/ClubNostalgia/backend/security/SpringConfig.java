package com.ClubNostalgia.backend.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.ClubNostalgia.backend.security.filter.JWTAuthentication;
import com.ClubNostalgia.backend.security.filter.JWTAuthorization;


@Configuration
@AllArgsConstructor
public class SpringConfig {

    private final CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JWTAuthentication jwtAuthentication = new JWTAuthentication(customAuthenticationManager);
        jwtAuthentication.setFilterProcessesUrl("/login");

        http
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .authorizeHttpRequests(request -> request
            .requestMatchers("/h2/**").permitAll()
            .requestMatchers("/").permitAll() 
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()  
            .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN")  
            .anyRequest().authenticated()
        )
        .addFilter(jwtAuthentication)
        .addFilterAfter(new JWTAuthorization(), JWTAuthentication.class)
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        return http.build();
    }
}