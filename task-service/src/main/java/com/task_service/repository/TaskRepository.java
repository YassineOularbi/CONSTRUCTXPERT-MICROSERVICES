package com.task_service.repository;

import com.task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long id);
    @Query("select t.id from Task t where t.projectId = ?1")
    List<Long> getIdsByProject(Long id);
}
