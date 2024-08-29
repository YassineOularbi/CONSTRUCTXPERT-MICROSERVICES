package com.task_service.mapper;

import com.task_service.dto.TaskDto;
import com.task_service.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);

    Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);
}
