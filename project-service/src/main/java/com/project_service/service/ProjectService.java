package com.project_service.service;

import com.project_service.dto.ProjectDto;
import com.project_service.exception.ProjectNotFoundException;
import com.project_service.mapper.ProjectMapper;
import com.project_service.model.Project;
import com.project_service.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final RestTemplate restTemplate;
    private final String TASK_SERVICE_URL = "http://task-service/api/tasks";

    public ProjectDto createProject(ProjectDto projectDto) {
        var project = projectMapper.toEntity(projectDto);
        var savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    public List<Project> getAllProjects() {
        var projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new ProjectNotFoundException("Projects not founds !");
        }
        return projects;
    }

    public ProjectDto getProjectById(Long id) {
        var project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(STR."Project with \{id} not found !"));
        return projectMapper.toDto(project);
    }

    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        var project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(STR."Project with \{id} not found !"));
        var updatedProject = projectMapper.partialUpdate(projectDto, project);
        var savedProject = projectRepository.save(updatedProject);
        return projectMapper.toDto(savedProject);
    }

    public void deleteProject(Long id) {
        var project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(STR."Project with \{id} not found !"));
        var tasks = restTemplate.getForObject(STR."\{TASK_SERVICE_URL}/get-tasks-ids-by-project/\{id}", List.class);

        if (tasks != null && !tasks.isEmpty()) {
            tasks.forEach(taskId ->
                    restTemplate.delete(STR."\{TASK_SERVICE_URL}/delete-task/\{taskId}")
            );
        }
        projectRepository.delete(project);
    }
}
