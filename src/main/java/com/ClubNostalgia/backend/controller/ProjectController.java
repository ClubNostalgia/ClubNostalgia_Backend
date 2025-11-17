package com.ClubNostalgia.backend.controller;

import com.ClubNostalgia.backend.service.interfaces.ProjectService;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.ClubNostalgia.backend.dto.request.ProjectRequest;
import com.ClubNostalgia.backend.dto.response.ProjectResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        ProjectResponse response = projectService.createProject(projectRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable UUID id) {
        return new ResponseEntity<>(projectService.getProjectById(id), HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<ProjectResponse> getProjectByTitle(@PathVariable String title) {
        return new ResponseEntity<>(projectService.getProjectByTitle(title), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable UUID id, @Valid @RequestBody ProjectRequest projectRequest) {
        ProjectResponse response = projectService.updateProject(id, projectRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}