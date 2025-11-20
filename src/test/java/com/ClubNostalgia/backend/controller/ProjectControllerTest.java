package com.ClubNostalgia.backend.controller;

import com.ClubNostalgia.backend.dto.request.ProjectRequest;
import com.ClubNostalgia.backend.dto.response.ProjectResponse;
import com.ClubNostalgia.backend.entity.Project.VideoType;
import com.ClubNostalgia.backend.service.interfaces.ProjectService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private ProjectRequest projectRequest;
    private ProjectResponse projectResponse;
    private UUID projectId;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        
        projectRequest = ProjectRequest.builder()
                .title("Test Project")
                .video("https://www.youtube.com/watch?v=test")
                .videoType(VideoType.YOUTUBE)
                .synopsis("Test synopsis")
                .information("Test information")
                .author("Test Author")
                .categoryId(categoryId)
                .build();
        
        projectResponse = ProjectResponse.builder()
                .id(projectId)
                .title("Test Project")
                .video("https://www.youtube.com/watch?v=test")
                .videoType(VideoType.YOUTUBE)
                .synopsis("Test synopsis")
                .information("Test information")
                .author("Test Author")
                .categoryId(categoryId)
                .categoryName("Test Category")
                .build();
    }

    @Test
    void createProject_DeberiaRetornarProjectResponseConStatusCreated_CuandoSeaCorrecto() {
        when(projectService.createProject(any(ProjectRequest.class))).thenReturn(projectResponse);

        ResponseEntity<ProjectResponse> response = projectController.createProject(projectRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(projectResponse, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(projectId, response.getBody().getId());
        assertEquals("Test Project", response.getBody().getTitle());
        assertEquals("Test Author", response.getBody().getAuthor());
        
        verify(projectService, times(1)).createProject(projectRequest);
    }

    @Test
    void createProject_DeberiaLlamarAlServicioConElRequestCorrecto() {
        when(projectService.createProject(any(ProjectRequest.class))).thenReturn(projectResponse);

        projectController.createProject(projectRequest);

        verify(projectService).createProject(projectRequest);
        verifyNoMoreInteractions(projectService);
    }

    @Test
    void getProjects_DeberiaRetornarListaDeProyectos_CuandoExistanProyectos() {
        ProjectResponse project1 = ProjectResponse.builder()
                .id(UUID.randomUUID())
                .title("Project 1")
                .video("https://www.youtube.com/watch?v=test1")
                .videoType(VideoType.YOUTUBE)
                .synopsis("Synopsis 1")
                .information("Info 1")
                .author("Author 1")
                .build();
        
        ProjectResponse project2 = ProjectResponse.builder()
                .id(UUID.randomUUID())
                .title("Project 2")
                .video("https://vimeo.com/test2")
                .videoType(VideoType.VIMEO)
                .synopsis("Synopsis 2")
                .information("Info 2")
                .author("Author 2")
                .build();
        
        List<ProjectResponse> projectsList = Arrays.asList(project1, project2);
        when(projectService.getProjects()).thenReturn(projectsList);

        ResponseEntity<List<ProjectResponse>> response = projectController.getProjects();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(projectsList, response.getBody());
        
        verify(projectService, times(1)).getProjects();
    }

    @Test
    void getProjects_DeberiaRetornarListaVacia_CuandoNoHayaProyectos() {
        List<ProjectResponse> emptyList = Arrays.asList();
        when(projectService.getProjects()).thenReturn(emptyList);

        ResponseEntity<List<ProjectResponse>> response = projectController.getProjects();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        
        verify(projectService, times(1)).getProjects();
    }

    @Test
    void getProjectById_DeberiaRetornarProyecto_CuandoElIdExista() {
        when(projectService.getProjectById(projectId)).thenReturn(projectResponse);

        ResponseEntity<ProjectResponse> response = projectController.getProjectById(projectId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(projectId, response.getBody().getId());
        assertEquals("Test Project", response.getBody().getTitle());
        assertEquals("Test Author", response.getBody().getAuthor());
        
        verify(projectService, times(1)).getProjectById(projectId);
    }

    @Test
    void getProjectById_DeberiaLlamarAlServicioConElIdCorrecto() {
        UUID specificId = UUID.randomUUID();
        when(projectService.getProjectById(specificId)).thenReturn(projectResponse);

        projectController.getProjectById(specificId);

        verify(projectService).getProjectById(specificId);
        verify(projectService, times(1)).getProjectById(specificId);
    }

    @Test
    void getProjectByTitle_DeberiaRetornarProyecto_CuandoElTituloExista() {
        String title = "Test Project";
        when(projectService.getProjectByTitle(title)).thenReturn(projectResponse);

        ResponseEntity<ProjectResponse> response = projectController.getProjectByTitle(title);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(projectResponse, response.getBody());
        assertEquals("Test Project", response.getBody().getTitle());
        
        verify(projectService, times(1)).getProjectByTitle(title);
    }

    @Test
    void getProjectByTitle_DeberiaLlamarAlServicioConElTituloCorrecto() {
        String title = "Another Project";
        when(projectService.getProjectByTitle(title)).thenReturn(projectResponse);

        projectController.getProjectByTitle(title);

        verify(projectService).getProjectByTitle(title);
        verify(projectService).getProjectByTitle(eq(title));
        verifyNoMoreInteractions(projectService);
    }

    @Test
    void getProjectByTitle_DeberiaFuncionarConTitulosConEspacios() {
        String title = "Project With Spaces";
        when(projectService.getProjectByTitle(title)).thenReturn(projectResponse);

        ResponseEntity<ProjectResponse> response = projectController.getProjectByTitle(title);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(projectService).getProjectByTitle(title);
    }

    @Test
    void updateProject_DeberiaRetornarProjectResponseActualizado_CuandoSeaCorrecto() {
        when(projectService.updateProject(eq(projectId), any(ProjectRequest.class))).thenReturn(projectResponse);

        ResponseEntity<ProjectResponse> response = projectController.updateProject(projectId, projectRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projectResponse, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(projectId, response.getBody().getId());
        
        verify(projectService, times(1)).updateProject(projectId, projectRequest);
    }

    @Test
    void updateProject_DeberiaLlamarAlServicioConParametrosCorrectos() {
        when(projectService.updateProject(eq(projectId), any(ProjectRequest.class))).thenReturn(projectResponse);

        projectController.updateProject(projectId, projectRequest);

        verify(projectService).updateProject(projectId, projectRequest);
        verifyNoMoreInteractions(projectService);
    }

    @Test
    void updateProject_DeberiaActualizarTodosLosCampos() {
        ProjectRequest updateRequest = ProjectRequest.builder()
                .title("Updated Title")
                .video("https://www.youtube.com/watch?v=updated")
                .videoType(VideoType.VIMEO)
                .synopsis("Updated synopsis")
                .information("Updated information")
                .author("Updated Author")
                .categoryId(UUID.randomUUID())
                .build();

        ProjectResponse updatedResponse = ProjectResponse.builder()
                .id(projectId)
                .title("Updated Title")
                .video("https://www.youtube.com/watch?v=updated")
                .videoType(VideoType.VIMEO)
                .synopsis("Updated synopsis")
                .information("Updated information")
                .author("Updated Author")
                .build();

        when(projectService.updateProject(eq(projectId), any(ProjectRequest.class))).thenReturn(updatedResponse);

        ResponseEntity<ProjectResponse> response = projectController.updateProject(projectId, updateRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated Author", response.getBody().getAuthor());
        assertEquals(VideoType.VIMEO, response.getBody().getVideoType());
    }

    @Test
    void deleteProject_DeberiaRetornarNoContent_CuandoSeElimineCorrectamente() {
        doNothing().when(projectService).deleteProject(projectId);

        ResponseEntity<Void> response = projectController.deleteProject(projectId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(projectService, times(1)).deleteProject(projectId);
    }

    @Test
    void deleteProject_DeberiaLlamarAlServicioConElIdCorrecto() {
        UUID specificId = UUID.randomUUID();
        doNothing().when(projectService).deleteProject(specificId);

        projectController.deleteProject(specificId);

        verify(projectService).deleteProject(specificId);
        verify(projectService, times(1)).deleteProject(specificId);
        verifyNoMoreInteractions(projectService);
    }

    @Test
    void todosLosMetodos_DeberianRetornarResponseEntityNoNulo() {
        when(projectService.createProject(any())).thenReturn(projectResponse);
        when(projectService.getProjects()).thenReturn(Arrays.asList());
        when(projectService.getProjectById(any())).thenReturn(projectResponse);
        when(projectService.getProjectByTitle(any())).thenReturn(projectResponse);
        when(projectService.updateProject(any(), any())).thenReturn(projectResponse);
        doNothing().when(projectService).deleteProject(any());

        assertNotNull(projectController.createProject(projectRequest));
        assertNotNull(projectController.getProjects());
        assertNotNull(projectController.getProjectById(projectId));
        assertNotNull(projectController.getProjectByTitle("test"));
        assertNotNull(projectController.updateProject(projectId, projectRequest));
        assertNotNull(projectController.deleteProject(projectId));
    }
}