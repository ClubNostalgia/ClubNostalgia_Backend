package com.ClubNostalgia.backend.service.impl;

import com.ClubNostalgia.backend.repository.ProjectRepository;
import com.ClubNostalgia.backend.repository.CategoryRepository;
import com.ClubNostalgia.backend.service.interfaces.ProjectService;
import com.ClubNostalgia.backend.mapper.ProjectMapper;
import com.ClubNostalgia.backend.entity.Project;
import com.ClubNostalgia.backend.entity.Category;
import com.ClubNostalgia.backend.dto.request.ProjectRequest;
import com.ClubNostalgia.backend.dto.response.ProjectResponse;
import com.ClubNostalgia.backend.exception.ResourceNotFoundException;
import com.ClubNostalgia.backend.exception.DuplicateResourceException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
private final ProjectMapper projectMapper;

public ProjectServiceImpl(ProjectRepository projectRepository, CategoryRepository categoryRepository, ProjectMapper projectMapper) {
    this.projectRepository = projectRepository;
    this.categoryRepository = categoryRepository;
    this.projectMapper = projectMapper;
}

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest) {
        
        if (projectRepository.findByTitle(projectRequest.getTitle()).isPresent()) {
            throw new DuplicateResourceException(
                "El proyecto '" + projectRequest.getTitle() + "' ya existe."
            );
        }
        
        Project project = projectMapper.projectRequestToProject(projectRequest);
        
        if (projectRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(projectRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "ID", projectRequest.getCategoryId()));
            project.setCategory(category);
        }
        
        Project savedProject = projectRepository.save(project);
        return projectMapper.projectToProjectResponse(savedProject); 
    }

    @Override
    public ProjectResponse getProjectById(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> 
            new ResourceNotFoundException("Proyecto", "ID", id) 
        );
        return projectMapper.projectToProjectResponse(project); 
    }
    
    @Override
    public ProjectResponse getProjectByTitle(String title) {
        Project project = projectRepository.findByTitle(title).orElseThrow(() -> 
            new ResourceNotFoundException("Proyecto", "título", title) 
        );
        return projectMapper.projectToProjectResponse(project); 
    }

    @Override
    public List<ProjectResponse> getProjects() {
        return projectRepository.findAll().stream()
            .map(projectMapper::projectToProjectResponse)
            .collect(Collectors.toList()); 
    }

    @Override
    public ProjectResponse updateProject(UUID id, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(id).orElseThrow(() -> 
            new ResourceNotFoundException("Proyecto", "ID", id)
        );

        project.setTitle(projectRequest.getTitle());
        project.setVideo(projectRequest.getVideo());
        project.setVideoType(projectRequest.getVideoType());
        project.setSynopsis(projectRequest.getSynopsis());
        project.setInformation(projectRequest.getInformation());
        project.setAuthor(projectRequest.getAuthor());

        // NUEVO: Actualizar categoría si viene en el request
        if (projectRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(projectRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "ID", projectRequest.getCategoryId()));
            project.setCategory(category);
        }

        Project updatedProject = projectRepository.save(project);
        return projectMapper.projectToProjectResponse(updatedProject);
    }

    @Override
    public void deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proyecto", "ID", id);
        }
        projectRepository.deleteById(id);
    }
}