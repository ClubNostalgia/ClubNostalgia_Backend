package com.ClubNostalgia.backend.config;

import com.ClubNostalgia.backend.entity.Category;
import com.ClubNostalgia.backend.entity.User;
import com.ClubNostalgia.backend.repository.CategoryRepository;
import com.ClubNostalgia.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(Category.builder()
                    .name("Audiovisual")
                    .description("Proyectos de cine, cortometrajes y producción audiovisual")
                    .build());

            categoryRepository.save(Category.builder()
                    .name("Fotografía")
                    .description("Sesiones fotográficas y proyectos de fotografía")
                    .build());

            categoryRepository.save(Category.builder()
                    .name("Teatro")
                    .description("Obras teatrales y producción escénica")
                    .build());

            System.out.println("Categorías inicializadas correctamente");
        }

        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@clubnostalgia.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .build();

            userRepository.save(admin);

            System.out.println("   Usuario admin creado:");
            System.out.println("   Email: admin@clubnostalgia.com");
            System.out.println("   Password: admin123");
        }
    }
}