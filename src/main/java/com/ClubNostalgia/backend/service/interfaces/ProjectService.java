package com.ClubNostalgia.backend.service.interfaces;

import com.ClubNostalgia.backend.dto.request.ProjectRequest;
import com.ClubNostalgia.backend.dto.response.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest projectRequest);
    ProjectResponse getProjectById(UUID id);
    ProjectResponse getProjectByTitle(String title);
    List<ProjectResponse> getProjects();
    ProjectResponse updateProject(UUID id, ProjectRequest projectRequest);
    void deleteProject(UUID id);
}