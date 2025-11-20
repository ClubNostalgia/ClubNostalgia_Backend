package com.ClubNostalgia.backend.service.impl;

import com.ClubNostalgia.backend.dto.request.UserRequest;
import com.ClubNostalgia.backend.dto.response.UserResponse;
import com.ClubNostalgia.backend.entity.User;
import com.ClubNostalgia.backend.exception.DuplicateResourceException;
import com.ClubNostalgia.backend.exception.ResourceNotFoundException;
import com.ClubNostalgia.backend.mapper.UserMapper;
import com.ClubNostalgia.backend.repository.UserRepository;
import com.ClubNostalgia.backend.security.UserDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userRequest = UserRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .role("ADMIN")
                .build();

        user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("hashedPassword")
                .role("ADMIN")
                .build();

        userResponse = UserResponse.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .role("ADMIN")
                .build();
    }

    @Test
    void loadUserByUsername_DeberiaRetornarUserDetails_CuandoElEmailExista() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("test@example.com");

        assertNotNull(result);
        assertInstanceOf(UserDetail.class, result);
        assertEquals("test@example.com", result.getUsername());
        
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_DeberiaLanzarUsernameNotFoundException_CuandoElEmailNoExista() {
        when(userRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> 
            userService.loadUserByUsername("noexiste@example.com")
        );

        verify(userRepository, times(1)).findByEmail("noexiste@example.com");
    }

    @Test
    void loadUserByUsername_DeberiaTenerMensajeDeErrorCorrecto() {
        String email = "noexiste@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class, 
            () -> userService.loadUserByUsername(email)
        );

        assertTrue(exception.getMessage().contains(email));
        assertTrue(exception.getMessage().contains("Usuario no encontrado con email"));
    }

    @Test
    void createAdmin_DeberiaCrearUsuarioCorrectamente_CuandoElEmailNoExista() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashedPassword");
        when(userMapper.userRequestToUser(userRequest, "hashedPassword", "ADMIN")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createAdmin(userRequest);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getName(), result.getName());
        assertEquals(userResponse.getEmail(), result.getEmail());
        assertEquals("ADMIN", result.getRole());

        verify(userRepository, times(1)).findByEmail(userRequest.getEmail());
        verify(passwordEncoder, times(1)).encode(userRequest.getPassword());
        verify(userMapper, times(1)).userRequestToUser(userRequest, "hashedPassword", "ADMIN");
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).userToUserResponse(user);
    }

    @Test
    void createAdmin_DeberiaLanzarDuplicateResourceException_CuandoElEmailYaExista() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(DuplicateResourceException.class, () -> 
            userService.createAdmin(userRequest)
        );

        verify(userRepository, times(1)).findByEmail(userRequest.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createAdmin_DeberiaCodificarLaContrasena() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userMapper.userRequestToUser(userRequest, "hashedPassword", "ADMIN")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        userService.createAdmin(userRequest);

        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void createAdmin_DeberiaAsignarRolAdmin() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userMapper.userRequestToUser(userRequest, "hashedPassword", "ADMIN")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        userService.createAdmin(userRequest);

        verify(userMapper, times(1)).userRequestToUser(userRequest, "hashedPassword", "ADMIN");
    }

    @Test
    void getUserById_DeberiaRetornarUsuario_CuandoElIdExista() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).userToUserResponse(user);
    }

    @Test
    void getUserById_DeberiaLanzarResourceNotFoundException_CuandoElIdNoExista() {
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            userService.getUserById(nonExistentId)
        );

        verify(userRepository, times(1)).findById(nonExistentId);
        verify(userMapper, never()).userToUserResponse(any());
    }

    @Test
    void getUserById_DeberiaTenerMensajeDeErrorCorrecto() {
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> userService.getUserById(nonExistentId)
        );

        assertTrue(exception.getMessage().contains("Usuario"));
        assertTrue(exception.getMessage().contains("ID"));
    }

    @Test
    void getUserByName_DeberiaRetornarUsuario_CuandoElNombreExista() {
        when(userRepository.findByName("Test User")).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserByName("Test User");

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository, times(1)).findByName("Test User");
        verify(userMapper, times(1)).userToUserResponse(user);
    }

    @Test
    void getUserByName_DeberiaLanzarResourceNotFoundException_CuandoElNombreNoExista() {
        when(userRepository.findByName("Usuario Inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            userService.getUserByName("Usuario Inexistente")
        );

        verify(userRepository, times(1)).findByName("Usuario Inexistente");
        verify(userMapper, never()).userToUserResponse(any());
    }

    @Test
    void getUserByName_DeberiaTenerMensajeDeErrorCorrecto() {
        String name = "Usuario Inexistente";
        when(userRepository.findByName(name)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> userService.getUserByName(name)
        );

        assertTrue(exception.getMessage().contains("Usuario"));
        assertTrue(exception.getMessage().contains("nombre"));
        assertTrue(exception.getMessage().contains(name));
    }

    @Test
    void getUsers_DeberiaRetornarListaDeUsuarios_CuandoExistanUsuarios() {
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .name("User 2")
                .email("user2@example.com")
                .password("password")
                .role("USER")
                .build();

        UserResponse userResponse2 = UserResponse.builder()
                .id(user2.getId())
                .name("User 2")
                .email("user2@example.com")
                .role("USER")
                .build();

        List<User> users = Arrays.asList(user, user2);
        
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);
        when(userMapper.userToUserResponse(user2)).thenReturn(userResponse2);

        List<UserResponse> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0).getName());
        assertEquals("User 2", result.get(1).getName());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).userToUserResponse(any());
    }

    @Test
    void getUsers_DeberiaRetornarListaVacia_CuandoNoHayaUsuarios() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        List<UserResponse> result = userService.getUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, never()).userToUserResponse(any());
    }

    @Test
    void getUsers_DeberiaMapearTodosLosUsuarios() {
        List<User> users = Arrays.asList(user, user, user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.userToUserResponse(any())).thenReturn(userResponse);

        List<UserResponse> result = userService.getUsers();

        assertEquals(3, result.size());
        verify(userMapper, times(3)).userToUserResponse(any());
    }
}
