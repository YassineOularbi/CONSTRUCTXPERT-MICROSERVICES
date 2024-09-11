package com.task_service.service;

import com.task_service.client.RequestContext;
import com.task_service.dto.TaskDto;
import com.task_service.exception.TaskNotFoundException;
import com.task_service.mapper.TaskMapper;
import com.task_service.model.Task;
import com.task_service.repository.TaskRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final RestTemplate restTemplate;
    private final String PROJECT_SERVICE_URL = "http://project-service/api/project";
    private final String RESOURCE_SERVICE_URL = "http://resource-service/api/resources";

    private HttpEntity<Void> createHttpEntity() {
        String token = RequestContext.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    @CircuitBreaker(name = "resourceServiceCircuitBreaker", fallbackMethod = "resourceServiceFallback")
    public TaskDto createTask(TaskDto taskDto) {
        for (Long resourceId : taskDto.getRIds()) {
            var resourceResult = restTemplate.exchange(
                    String.format("%s/get-resource-by-id/%d", RESOURCE_SERVICE_URL, resourceId),
                    HttpMethod.GET,
                    createHttpEntity(),
                    Void.class
            );
            if (resourceResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException("Resource not found!");
            }
        }
        var projectResult = restTemplate.exchange(
                String.format("%s/get-project-by-id/%d", PROJECT_SERVICE_URL, taskDto.getProjectId()),
                HttpMethod.GET,
                createHttpEntity(),
                Void.class
        );
        if (projectResult.getStatusCode().is2xxSuccessful()) {
            var task = taskMapper.toEntity(taskDto);
            task.setResourceIds(taskDto.getRIds().stream().map(Objects::toString).collect(Collectors.joining(", ")));
            var savedTask = taskRepository.save(task);
            var mappedTask = taskMapper.toDto(savedTask);
            mappedTask.setRIds((ArrayList<Long>) Arrays.stream(savedTask.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
            return mappedTask;
        } else {
            throw new RuntimeException("Project not found!");
        }
    }

    @CircuitBreaker(name = "resourceServiceCircuitBreaker", fallbackMethod = "resourceServiceFallback")
    public TaskDto getTaskById(Long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with %d not found!", id)));
        var mappedTask = taskMapper.toDto(task);
        if (task.getResourceIds() != null){
            mappedTask.setRIds((ArrayList<Long>) Arrays.stream(task.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
        }
        return mappedTask;
    }

    @CircuitBreaker(name = "projectServiceCircuitBreaker", fallbackMethod = "projectServiceFallback")
    public List<Task> getTasksByProjectId(Long projectId) {
        var projectResult = restTemplate.exchange(
                String.format("%s/get-project-by-id/%d", PROJECT_SERVICE_URL, projectId),
                HttpMethod.GET,
                createHttpEntity(),
                Void.class
        );
        if (projectResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            throw new RuntimeException("Project not found!");
        }
        var tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("Tasks not found!");
        }
        return tasks;
    }

    @CircuitBreaker(name = "projectServiceCircuitBreaker", fallbackMethod = "projectServiceFallback")
    public List<Long> getTasksIdsByProjectId(Long projectId) {
        var projectResult = restTemplate.exchange(
                String.format("%s/get-project-by-id/%d", PROJECT_SERVICE_URL, projectId),
                HttpMethod.GET,
                createHttpEntity(),
                Void.class
        );
        if (projectResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            throw new RuntimeException("Project not found!");
        }
        var tasksIds = taskRepository.getIdsByProject(projectId);
        if (tasksIds.isEmpty()) {
            throw new TaskNotFoundException("Tasks ids not found!");
        }
        return tasksIds;
    }

    @CircuitBreaker(name = "resourceServiceCircuitBreaker", fallbackMethod = "resourceServiceFallback")
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with %d not found!", id)));
        for (Long resourceId : taskDto.getRIds()) {
            var resourceResult = restTemplate.exchange(
                    String.format("%s/get-resource-by-id/%d", RESOURCE_SERVICE_URL, resourceId),
                    HttpMethod.GET,
                    createHttpEntity(),
                    Void.class
            );
            if (resourceResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException("Resource not found!");
            }
        }
        var projectResult = restTemplate.exchange(
                String.format("%s/get-project-by-id/%d", PROJECT_SERVICE_URL, taskDto.getProjectId()),
                HttpMethod.GET,
                createHttpEntity(),
                Void.class
        );
        if (projectResult.getStatusCode().is2xxSuccessful()) {
            var updatedTask = taskMapper.partialUpdate(taskDto, task);
            updatedTask.setResourceIds(taskDto.getRIds().stream().map(Objects::toString).collect(Collectors.joining(", ")));
            var savedTask = taskRepository.save(updatedTask);
            var mappedTask = taskMapper.toDto(savedTask);
            mappedTask.setRIds((ArrayList<Long>) Arrays.stream(savedTask.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
            return mappedTask;
        } else {
            throw new RuntimeException("Project not found!");
        }
    }

    public void deleteTask(Long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with %d not found!", id)));
        taskRepository.delete(task);
    }

    public String resourceServiceFallback(Long resourceId, Throwable throwable) {
        return "Resource service is temporarily unavailable. Please try again later.";
    }

    public String projectServiceFallback(Long projectId, Throwable throwable) {
        return "Project service is temporarily unavailable. Please try again later.";
    }
}
