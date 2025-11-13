package com.ClubNostalgia.backend.service.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import com.ClubNostalgia.backend.dto.request.UserRequest;
import com.ClubNostalgia.backend.dto.response.UserResponse;

public interface UserService {

    UserResponse createAdmin(UserRequest userRequest);
    UserResponse getUserByName(String name);
    UserResponse getUserById(UUID id);
    List<UserResponse> getUsers();
    UserDetails loadUserByUsername(String username);
}