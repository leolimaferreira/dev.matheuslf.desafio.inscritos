package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.exception.ProjectWithActiveTasksException;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.validator.ProjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskService taskService;
    private final ProjectValidator projectValidator;

    public ProjectResponseDTO saveProject(ProjectRequestDTO dto) {
        Project project = projectMapper.toEntity(dto);
        projectValidator.validate(project);

        if (dto.endDate().isBefore(project.getStartDate())) {
            throw new ConflictException("Project's start date must be before the end date.");
        }

        Project savedProject = projectRepository.save(project);
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

    public ProjectResponseDTO updateProject(UUID id, UpdateProjectDTO dto) {
        Project project = projectRepository.findById(id).orElseThrow( () -> new NotFoundException("Project not found"));
        projectValidator.validate(project);

        if (project.getStartDate().isBefore(LocalDate.now())) {
            throw new ConflictException("You cannot update a project that has been already started.");
        }

        if (project.getEndDate().isBefore(dto.startDate()) || dto.endDate().isBefore(project.getStartDate())) {
            throw new ConflictException("Project's start date must be before the end date.");
        }

        projectMapper.updateEntity(project, dto);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDTO(updatedProject);
    }

}
