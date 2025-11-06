package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponseDTO saveProject(ProjectRequestDTO dto) {
        Project entity = projectMapper.toEntity(dto);
        Project savedProject = projectRepository.save(entity);
        return projectMapper.toDTO(savedProject);
    }

    public Page<ProjectResponseDTO> findAllProjects(Integer page, Integer size) {
        Pageable pageRequest = Pageable.ofSize(size).withPage(page);
        Page<Project> projects = projectRepository.findAll(pageRequest);
        return projects.map(projectMapper::toDTO);
    }

}
