package com.ClubNostalgia.backend.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ClubNostalgia.backend.security.filter.JWTAuthentication;
import com.ClubNostalgia.backend.security.filter.JWTAuthorization;

import java.util.Arrays;
import java.util.List;


@Configuration
@AllArgsConstructor
public class SpringConfig {

    private final CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JWTAuthentication jwtAuthentication = new JWTAuthentication(customAuthenticationManager);
        jwtAuthentication.setFilterProcessesUrl("/login");

        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))  
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .authorizeHttpRequests(request -> request
            .requestMatchers("/h2/**").permitAll()
            .requestMatchers("/").permitAll() 
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/projects/**").permitAll()  
            .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN")
            .anyRequest().authenticated()
        )
        .addFilter(jwtAuthentication)
        .addFilterAfter(new JWTAuthorization(), JWTAuthentication.class)
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        return http.build();
    }
}