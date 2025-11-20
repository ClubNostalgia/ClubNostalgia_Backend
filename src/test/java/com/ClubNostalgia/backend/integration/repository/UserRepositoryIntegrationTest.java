package com.ClubNostalgia.backend.integration.repository;

import com.ClubNostalgia.backend.entity.User;
import com.ClubNostalgia.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        user1 = User.builder()
                .name("Juan Pérez")
                .email("juan@example.com")
                .role("ADMIN")
                .password("password123")
                .build();

        user2 = User.builder()
                .name("María García")
                .email("maria@example.com")
                .role("USER")
                .password("password456")
                .build();

        user3 = User.builder()
                .name("Pedro López")
                .email("pedro@example.com")
                .role("USER")
                .password("password789")
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();
    }

    @Test
    void findByName_DeberiaRetornarUsuario_CuandoElNombreExista() {
        Optional<User> result = userRepository.findByName("Juan Pérez");

        assertTrue(result.isPresent());
        assertEquals("Juan Pérez", result.get().getName());
        assertEquals("juan@example.com", result.get().getEmail());
        assertEquals("ADMIN", result.get().getRole());
    }

    @Test
    void findByName_DeberiaRetornarEmpty_CuandoElNombreNoExista() {
        Optional<User> result = userRepository.findByName("Usuario Inexistente");

        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());
    }

    @Test
    void findByName_DeberiaSerCaseSensitive() {
        Optional<User> result = userRepository.findByName("juan pérez");

        assertFalse(result.isPresent());
    }

    @Test
    void findByName_DeberiaFuncionarConNombresConEspacios() {
        Optional<User> result = userRepository.findByName("María García");

        assertTrue(result.isPresent());
        assertEquals("María García", result.get().getName());
    }

    @Test
    void findByEmail_DeberiaRetornarUsuario_CuandoElEmailExista() {
        Optional<User> result = userRepository.findByEmail("juan@example.com");

        assertTrue(result.isPresent());
        assertEquals("juan@example.com", result.get().getEmail());
        assertEquals("Juan Pérez", result.get().getName());
        assertEquals("ADMIN", result.get().getRole());
    }

    @Test
    void findByEmail_DeberiaRetornarEmpty_CuandoElEmailNoExista() {
        Optional<User> result = userRepository.findByEmail("noexiste@example.com");

        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmail_DeberiaSerCaseSensitive() {
        Optional<User> result = userRepository.findByEmail("JUAN@EXAMPLE.COM");

        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_DeberiaSerUnico() {
        long count = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("juan@example.com"))
                .count();

        assertEquals(1, count);
    }

    @Test
    void save_DeberiaGuardarUsuarioCorrectamente() {
        User newUser = User.builder()
                .name("Nuevo Usuario")
                .email("nuevo@example.com")
                .role("USER")
                .password("newpassword")
                .build();

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("Nuevo Usuario", savedUser.getName());
        assertEquals("nuevo@example.com", savedUser.getEmail());
    }

    @Test
    void save_DeberiaGenerarIdAutomaticamente() {
        User newUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .role("USER")
                .password("testpassword")
                .build();

        assertNull(newUser.getId());

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId());
    }

    @Test
    void findAll_DeberiaRetornarTodosLosUsuarios() {
        var users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    void findById_DeberiaRetornarUsuario_CuandoElIdExista() {
        Optional<User> result = userRepository.findById(user1.getId());

        assertTrue(result.isPresent());
        assertEquals(user1.getId(), result.get().getId());
        assertEquals("Juan Pérez", result.get().getName());
    }

    @Test
    void findById_DeberiaRetornarEmpty_CuandoElIdNoExista() {
        Optional<User> result = userRepository.findById(java.util.UUID.randomUUID());

        assertFalse(result.isPresent());
    }

    @Test
    void delete_DeberiaEliminarUsuarioCorrectamente() {
        userRepository.delete(user1);

        Optional<User> result = userRepository.findById(user1.getId());
        assertFalse(result.isPresent());

        assertEquals(2, userRepository.findAll().size());
    }

    @Test
    void update_DeberiaActualizarUsuarioCorrectamente() {
        user1.setName("Juan Actualizado");
        user1.setEmail("juan.actualizado@example.com");

        User updatedUser = userRepository.save(user1);

        assertEquals("Juan Actualizado", updatedUser.getName());
        assertEquals("juan.actualizado@example.com", updatedUser.getEmail());

        Optional<User> result = userRepository.findById(user1.getId());
        assertTrue(result.isPresent());
        assertEquals("Juan Actualizado", result.get().getName());
    }

    @Test
    void count_DeberiaRetornarNumeroCorrecto() {
        long count = userRepository.count();

        assertEquals(3, count);
    }

    @Test
    void existsById_DeberiaRetornarTrue_CuandoElUsuarioExista() {
        boolean exists = userRepository.existsById(user1.getId());

        assertTrue(exists);
    }

    @Test
    void existsById_DeberiaRetornarFalse_CuandoElUsuarioNoExista() {
        boolean exists = userRepository.existsById(java.util.UUID.randomUUID());

        assertFalse(exists);
    }
}