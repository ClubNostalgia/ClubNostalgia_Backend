package com.ClubNostalgia.backend.security;

import com.ClubNostalgia.backend.service.interfaces.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final UserService userService; 
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username); 
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Email o contraseña inválida.");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Email o contraseña inválida.");
        }

        return new UsernamePasswordAuthenticationToken(
            username, 
            userDetails.getPassword(), 
            userDetails.getAuthorities()
        );
    }
}