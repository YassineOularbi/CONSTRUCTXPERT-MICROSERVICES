package com.task_service.controller;

import com.task_service.dto.TaskDto;
import com.task_service.exception.TaskNotFoundException;
import com.task_service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create-task")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        try {
            var createdTask = taskService.createTask(taskDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/get-task-by-id/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable("id") Long id) {
        try {
            var taskDto = taskService.getTaskById(id);
            return ResponseEntity.ok(taskDto);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-tasks-by-project/{projectId}")
    public ResponseEntity<?> getTasksByProjectId(@PathVariable("projectId") Long projectId) {
        try {
            var tasks = taskService.getTasksByProjectId(projectId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-tasks-ids-by-project/{projectId}")
    public List<Long> getTasksIdsByProjectId(@PathVariable("projectId") Long projectId) {
        return taskService.getTasksIdsByProjectId(projectId);
    }

    @PutMapping("/update-task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
        try {
            var updatedTask = taskService.updateTask(id, taskDto);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
