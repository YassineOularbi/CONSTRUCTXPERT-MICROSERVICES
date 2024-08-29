package com.resource_service.mapper;

import com.resource_service.dto.ResourceDto;
import com.resource_service.model.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    Resource toEntity(ResourceDto resourceDto);
    ResourceDto toDto(Resource resource);
    Resource partialUpdate(ResourceDto resourceDto, @MappingTarget Resource resource);
}
