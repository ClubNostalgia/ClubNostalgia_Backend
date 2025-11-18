package com.ClubNostalgia.backend.config;

import com.ClubNostalgia.backend.entity.Category;
import com.ClubNostalgia.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

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
    }
}