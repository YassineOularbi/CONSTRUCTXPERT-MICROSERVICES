package com.project_service.controller;

import com.project_service.dto.ProjectDto;
import com.project_service.exception.ProjectNotFoundException;
import com.project_service.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get-all-projects")
    public ResponseEntity<?> getAllProjects() {
        try {
            var projects = projectService.getAllProjects();
            return ResponseEntity.ok(projects);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-project-by-id/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable("id") String id) {
        try {
            var project = projectService.getProjectById(Long.valueOf(id));
            return ResponseEntity.ok(project);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/create-project")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        try {
            var createdProject = projectService.createProject(projectDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-project/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable("id") String id,
            @ModelAttribute ProjectDto projectDto) {
        try {
            var updatedProject = projectService.updateProject(Long.valueOf(id), projectDto);
            return ResponseEntity.ok(updatedProject);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-project/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") String id) {
        try {
            projectService.deleteProject(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
