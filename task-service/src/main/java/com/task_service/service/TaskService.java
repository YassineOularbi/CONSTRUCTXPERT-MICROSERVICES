package com.task_service.service;

import com.task_service.dto.TaskDto;
import com.task_service.exception.TaskNotFoundException;
import com.task_service.mapper.TaskMapper;
import com.task_service.model.Task;
import com.task_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public TaskDto createTask(TaskDto taskDto) {
        for (Long resourceId : taskDto.getRIds()) {
            var resourceResult = restTemplate.getForEntity(STR."\{RESOURCE_SERVICE_URL}/get-resource-by-id/\{resourceId}", Void.class);
            if (resourceResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException();
            }
        }
       var result = restTemplate.getForEntity(STR."\{PROJECT_SERVICE_URL}/get-project-by-id/\{taskDto.getProjectId()}", Void.class);
       if (result.getStatusCode().is2xxSuccessful()){
           var task = taskMapper.toEntity(taskDto);
           task.setResourceIds(taskDto.getRIds().stream().map(Objects::toString).collect(Collectors.joining(", ")));
           var savedTask = taskRepository.save(task);
           var mappedTask = taskMapper.toDto(savedTask);
           mappedTask.setRIds((ArrayList<Long>) Arrays.stream(savedTask.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
           return mappedTask;
       } else {
           throw new RuntimeException();
       }
    }

    public TaskDto getTaskById(Long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(STR."Task with \{id} not found !"));
        var mappedTask = taskMapper.toDto(task);
        if (task.getResourceIds() != null){
            mappedTask.setRIds((ArrayList<Long>) Arrays.stream(task.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
        }
        return mappedTask;
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        var result = restTemplate.getForEntity(STR."\{PROJECT_SERVICE_URL}/get-project-by-id/\{projectId}", Void.class);
        if (result.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)){
            throw new RuntimeException();
        }
        var tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("Tasks not founds !");
        }
        return tasks;
    }

    public List<Long> getTasksIdsByProjectId(Long projectId) {
        var result = restTemplate.getForEntity(STR."\{PROJECT_SERVICE_URL}/get-project-by-id/\{projectId}", Void.class);
        if (result.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)){
            throw new RuntimeException();
        }
        var tasksIds = taskRepository.getIdsByProject(projectId);
        if (tasksIds.isEmpty()) {
            throw new TaskNotFoundException("Tasks ids not founds !");
        }
        return tasksIds;
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(STR."Task with \{id} not found!"));
        for (Long resourceId : taskDto.getRIds()) {
            var resourceResult = restTemplate.getForEntity(STR."\{RESOURCE_SERVICE_URL}/get-resource-by-id/\{resourceId}", Void.class);
            if (resourceResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException();
            }
        }
        var projectResult = restTemplate.getForEntity(STR."\{PROJECT_SERVICE_URL}/get-project-by-id/\{taskDto.getProjectId()}", Void.class);
        if (projectResult.getStatusCode().is2xxSuccessful()) {
            var updatedTask = taskMapper.partialUpdate(taskDto, task);
            updatedTask.setResourceIds(taskDto.getRIds().stream().map(Objects::toString).collect(Collectors.joining(", ")));
            var savedTask = taskRepository.save(updatedTask);
            var mappedTask = taskMapper.toDto(savedTask);
            mappedTask.setRIds((ArrayList<Long>) Arrays.stream(savedTask.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
            return mappedTask;
        } else {
            throw new RuntimeException();
        }
    }


    public void deleteTask(Long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(STR."Task with \{id} not found !"));
        taskRepository.delete(task);
    }
}
