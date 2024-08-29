package com.project_service.mapper;

import com.project_service.dto.ProjectDto;
import com.project_service.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toEntity(ProjectDto projectDto);
    ProjectDto toDto(Project project);
    Project partialUpdate(ProjectDto projectDto, @MappingTarget Project project);
}
