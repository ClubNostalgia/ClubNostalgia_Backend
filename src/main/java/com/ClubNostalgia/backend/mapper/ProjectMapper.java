package com.ClubNostalgia.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ClubNostalgia.backend.dto.request.ProjectRequest;
import com.ClubNostalgia.backend.dto.response.ProjectResponse;
import com.ClubNostalgia.backend.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper{
    
    ProjectResponse projectToProjectResponse(Project project);

    @Mapping(target = "id", ignore = true)
    Project projectRequestToProject(ProjectRequest projectRequest);
}