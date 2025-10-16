package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ProjectResponseDTO> findAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDTO)
                .toList();
    }

}
