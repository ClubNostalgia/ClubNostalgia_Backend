package com.ClubNostalgia.backend.controller;

import com.ClubNostalgia.backend.entity.Category;
import com.ClubNostalgia.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    void setUp() {
        category1 = Category.builder()
                .id(UUID.randomUUID())
                .name("Cine")
                .description("Proyectos relacionados con el cine")
                .build();

        category2 = Category.builder()
                .id(UUID.randomUUID())
                .name("Música")
                .description("Proyectos relacionados con la música")
                .build();

        category3 = Category.builder()
                .id(UUID.randomUUID())
                .name("Arte")
                .description("Proyectos relacionados con el arte")
                .build();
    }

    @Test
    void getAllCategories_DeberiaRetornarListaDeCategorias_CuandoExistanCategorias() {
        List<Category> categoriesList = Arrays.asList(category1, category2, category3);
        when(categoryRepository.findAll()).thenReturn(categoriesList);

        List<Category> result = categoryController.getAllCategories();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(categoriesList, result);
        assertEquals("Cine", result.get(0).getName());
        assertEquals("Música", result.get(1).getName());
        assertEquals("Arte", result.get(2).getName());
        
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getAllCategories_DeberiaRetornarListaVacia_CuandoNoHayaCategorias() {
        List<Category> emptyList = Arrays.asList();
        when(categoryRepository.findAll()).thenReturn(emptyList);

        List<Category> result = categoryController.getAllCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getAllCategories_DeberiaLlamarAlRepositorioUnaVez() {
        List<Category> categoriesList = Arrays.asList(category1);
        when(categoryRepository.findAll()).thenReturn(categoriesList);

        categoryController.getAllCategories();

        verify(categoryRepository).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getAllCategories_DeberiaRetornarCategoriasConTodosLosCampos() {
        List<Category> categoriesList = Arrays.asList(category1);
        when(categoryRepository.findAll()).thenReturn(categoriesList);

        List<Category> result = categoryController.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        
        Category category = result.get(0);
        assertNotNull(category.getId());
        assertEquals("Cine", category.getName());
        assertEquals("Proyectos relacionados con el cine", category.getDescription());
    }

    @Test
    void getAllCategories_DeberiaRetornarMultiplesCategorias() {
        List<Category> categoriesList = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categoriesList);

        List<Category> result = categoryController.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotEquals(result.get(0).getId(), result.get(1).getId());
        assertNotEquals(result.get(0).getName(), result.get(1).getName());
    }

    @Test
    void getAllCategories_NoDeberiaRetornarNull() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList());

        List<Category> result = categoryController.getAllCategories();

        assertNotNull(result);
    }
}
