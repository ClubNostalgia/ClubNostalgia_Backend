package com.ClubNostalgia.backend.service.impl;

import com.ClubNostalgia.backend.repository.UserRepository;
import com.ClubNostalgia.backend.service.interfaces.UserService;
import com.ClubNostalgia.backend.mapper.UserMapper;
import com.ClubNostalgia.backend.entity.User;
import com.ClubNostalgia.backend.dto.request.UserRequest;
import com.ClubNostalgia.backend.dto.response.UserResponse;
import com.ClubNostalgia.backend.exception.ResourceNotFoundException;
import com.ClubNostalgia.backend.exception.DuplicateResourceException;
import com.ClubNostalgia.backend.security.UserDetail;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email) 
            .orElseThrow(() -> 
                new UsernameNotFoundException("Usuario no encontrado con email: " + email)
            );
        return new UserDetail(user); 
    }


@Override
    public UserResponse createAdmin(UserRequest userRequest) {
        
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new DuplicateResourceException(
                "El email '" + userRequest.getEmail() + "' ya está registrado."
            );
        }
        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = userMapper.userRequestToUser(userRequest, hashedPassword, "ADMIN");
    
        User savedUser = userRepository.save(user);
        return userMapper.userToUserResponse(savedUser); 
    }

@Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> 
            new ResourceNotFoundException("Usuario", "ID", id) 
        );
        return userMapper.userToUserResponse(user); 
    }
    
    @Override
    public UserResponse getUserByName(String name) {
        User user = userRepository.findByName(name).orElseThrow(() -> 
            new ResourceNotFoundException("Usuario", "nombre", name) 
        );
        return userMapper.userToUserResponse(user); 
    }
    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::userToUserResponse)
            .collect(Collectors.toList()); 
    }
}