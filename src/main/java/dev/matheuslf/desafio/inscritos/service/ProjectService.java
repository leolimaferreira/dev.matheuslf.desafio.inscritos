package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.exception.ProjectWithActiveTasksException;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskService taskService;

    public ProjectResponseDTO saveProject(ProjectRequestDTO dto) {
        if (projectRepository.existsByName(dto.name())) throw new ConflictException("There is already a project with this name.");
        Project entity = projectMapper.toEntity(dto);
        Project savedProject = projectRepository.save(entity);
        return projectMapper.toDTO(savedProject);
    }

    public Page<ProjectResponseDTO> findAllProjects(Integer page, Integer size) {
        Pageable pageRequest = Pageable.ofSize(size).withPage(page);
        Page<Project> projects = projectRepository.findAll(pageRequest);
        return projects.map(projectMapper::toDTO);
    }

    public void deleteProject(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow( () -> new NotFoundException("Project not found"));
        if (taskService.hasDoingTasks(project)) {
            throw new ProjectWithActiveTasksException("There are tasks with status DOING in this project");
        }
        projectRepository.delete(project);
        projectMapper.toDTO(project);
    }

}
