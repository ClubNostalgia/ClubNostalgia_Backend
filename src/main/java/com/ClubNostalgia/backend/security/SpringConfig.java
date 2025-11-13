package com.ClubNostalgia.backend.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ClubNostalgia.backend.security.filter.JWTAuthentication;
import com.ClubNostalgia.backend.security.filter.JWTAuthorization;


@Configuration
@AllArgsConstructor
public class SpringConfig {

    private final CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JWTAuthentication jwtAuthentication = new JWTAuthentication(customAuthenticationManager);
        jwtAuthentication.setFilterProcessesUrl("/login");

        http
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .authorizeHttpRequests(request -> request
            .requestMatchers("/h2/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/users").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated()
        )
        .addFilter(jwtAuthentication)
        .addFilterAfter(new JWTAuthorization(), JWTAuthentication.class)
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        return http.build();
    }
}