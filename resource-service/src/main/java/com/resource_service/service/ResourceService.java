package com.resource_service.service;

import com.resource_service.dto.ResourceDto;
import com.resource_service.exception.ResourceNotFoundException;
import com.resource_service.mapper.ResourceMapper;
import com.resource_service.model.Resource;
import com.resource_service.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    public ResourceDto createResource(ResourceDto resourceDto) {
        var resource = resourceMapper.toEntity(resourceDto);
        var savedResource = resourceRepository.save(resource);
        return resourceMapper.toDto(savedResource);
    }

    public ResourceDto getResourceById(Long id) {
        var resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(STR."Resource with \{id} not found !"));
        return resourceMapper.toDto(resource);
    }

    public List<Resource> getAllResources() {
        var resources = resourceRepository.findAll();
        if (resources.isEmpty()) {
            throw new ResourceNotFoundException("Tasks not founds");
        }
        return resources;
    }

    public ResourceDto updateResource(Long id, ResourceDto resourceDto) {
        var resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(STR."Resource with \{id} not found !"));
        var updatedResource = resourceMapper.partialUpdate(resourceDto, resource);
        var savedResource = resourceRepository.save(updatedResource);
        return resourceMapper.toDto(savedResource);
    }

    public void deleteResource(Long id) {
        var resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(STR."Resource with \{id} not found !"));
        resourceRepository.delete(resource);
    }
}
