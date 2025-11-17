package com.ClubNostalgia.backend.service.impl;

import com.ClubNostalgia.backend.dto.request.UserRequest;
import com.ClubNostalgia.backend.dto.response.UserResponse;
import com.ClubNostalgia.backend.entity.User;
import com.ClubNostalgia.backend.mapper.UserMapper;
import com.ClubNostalgia.backend.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private UserMapper userMapper;

        @InjectMocks
        private UserServiceImpl userService;

        @Test
        void shouldCreateAdminSuccessfully() {
                UserRequest userRequest = UserRequest.builder()
                                .name("Admin Test")
                                .email("admin@test.com")
                                .password("Password123")
                                .build();

                User user = User.builder()
                                .id(UUID.randomUUID())
                                .name("Admin Test")
                                .email("admin@test.com")
                                .password("$2a$10$hashedPassword")
                                .role("ADMIN")
                                .build();

                UserResponse userResponse = UserResponse.builder()
                                .id(user.getId())
                                .name("Admin Test")
                                .email("admin@test.com")
                                .role("ADMIN")
                                .build();

                when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
                when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("$2a$10$hashedPassword");
                when(userMapper.userRequestToUser(userRequest, "$2a$10$hashedPassword", "ADMIN")).thenReturn(user);
                when(userRepository.save(user)).thenReturn(user);
                when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

                UserResponse result = userService.createAdmin(userRequest);

                assertThat(result).isNotNull();
                assertThat(result.getName()).isEqualTo("Admin Test");
                assertThat(result.getEmail()).isEqualTo("admin@test.com");
                assertThat(result.getRole()).isEqualTo("ADMIN");
        }
}
