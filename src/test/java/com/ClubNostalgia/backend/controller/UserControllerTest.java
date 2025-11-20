package com.ClubNostalgia.backend.controller;

import com.ClubNostalgia.backend.dto.request.UserRequest;
import com.ClubNostalgia.backend.dto.response.UserResponse;
import com.ClubNostalgia.backend.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequest userRequest;
    private UserResponse userResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        userRequest = UserRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .role("ADMIN")
                .password("password123")
                .build();
        
        userResponse = UserResponse.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .role("ADMIN")
                .build();
    }

    @Test
    void createAdmin_DeberiaRetornarUserResponseConStatusCreated_CuandoSeaCorrecto() {
        when(userService.createAdmin(any(UserRequest.class))).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.createAdmin(userRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        assertEquals("Test User", response.getBody().getName());
        assertEquals("test@example.com", response.getBody().getEmail());
        
        verify(userService, times(1)).createAdmin(userRequest);
    }

    @Test
    void createAdmin_DeberiaLlamarAlServicioConElRequestCorrecto() {
        when(userService.createAdmin(any(UserRequest.class))).thenReturn(userResponse);

        userController.createAdmin(userRequest);

        verify(userService).createAdmin(userRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUsers_DeberiaRetornarListaDeUsuarios_CuandoExistanUsuarios() {
        UserResponse user1 = UserResponse.builder()
                .id(UUID.randomUUID())
                .name("User 1")
                .email("user1@example.com")
                .role("USER")
                .build();
        
        UserResponse user2 = UserResponse.builder()
                .id(UUID.randomUUID())
                .name("User 2")
                .email("user2@example.com")
                .role("ADMIN")
                .build();
        
        List<UserResponse> usersList = Arrays.asList(user1, user2);
        when(userService.getUsers()).thenReturn(usersList);

        ResponseEntity<List<UserResponse>> response = userController.getUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(usersList, response.getBody());
        
        verify(userService, times(1)).getUsers();
    }

    @Test
    void getUsers_DeberiaRetornarListaVacia_CuandoNoHayaUsuarios() {
        List<UserResponse> emptyList = Arrays.asList();
        when(userService.getUsers()).thenReturn(emptyList);

        ResponseEntity<List<UserResponse>> response = userController.getUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        
        verify(userService, times(1)).getUsers();
    }

    @Test
    void getUserById_DeberiaRetornarUsuario_CuandoElIdExista() {
        when(userService.getUserById(userId)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUserById(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        assertEquals("Test User", response.getBody().getName());
        assertEquals("test@example.com", response.getBody().getEmail());
        
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_DeberiaLlamarAlServicioConElIdCorrecto() {
        UUID specificId = UUID.randomUUID();
        when(userService.getUserById(specificId)).thenReturn(userResponse);

        userController.getUserById(specificId);

        verify(userService).getUserById(specificId);
        verify(userService, times(1)).getUserById(specificId);
    }

    @Test
    void getUserByName_DeberiaRetornarUsuario_CuandoElNombreExista() {
        String userName = "Test User";
        when(userService.getUserByName(userName)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUserByName(userName);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userResponse, response.getBody());
        assertEquals("Test User", response.getBody().getName());
        
        verify(userService, times(1)).getUserByName(userName);
    }

    @Test
    void getUserByName_DeberiaLlamarAlServicioConElNombreCorrecto() {
        String userName = "Juan";
        when(userService.getUserByName(userName)).thenReturn(userResponse);

        userController.getUserByName(userName);

        verify(userService).getUserByName(userName);
        verify(userService).getUserByName(eq(userName));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUserByName_DeberiaFuncionarConNombresConEspacios() {
        String userName = "Juan Pérez";
        when(userService.getUserByName(userName)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUserByName(userName);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).getUserByName(userName);
    }

    @Test
    void todosLosMetodos_DeberianRetornarResponseEntityNoNulo() {
        when(userService.createAdmin(any())).thenReturn(userResponse);
        when(userService.getUsers()).thenReturn(Arrays.asList());
        when(userService.getUserById(any())).thenReturn(userResponse);
        when(userService.getUserByName(any())).thenReturn(userResponse);

        assertNotNull(userController.createAdmin(userRequest));
        assertNotNull(userController.getUsers());
        assertNotNull(userController.getUserById(userId));
        assertNotNull(userController.getUserByName("test"));
    }
}