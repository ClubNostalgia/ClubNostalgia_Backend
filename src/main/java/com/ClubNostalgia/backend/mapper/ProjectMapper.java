package com.ClubNostalgia.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ClubNostalgia.backend.dto.request.ProjectRequest;
import com.ClubNostalgia.backend.dto.response.ProjectResponse;
import com.ClubNostalgia.backend.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
    
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProjectResponse projectToProjectResponse(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Project projectRequestToProject(ProjectRequest projectRequest);
}