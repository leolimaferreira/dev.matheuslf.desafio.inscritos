package dev.matheuslf.desafio.inscritos.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.*;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import dev.matheuslf.desafio.inscritos.validator.ProjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskService taskService;
    private final ProjectValidator projectValidator;
    private final UserRepository userRepository;

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

    public ProjectResponseDTO updateProject(String token, UUID id, UpdateProjectDTO dto) {
        Project project = projectRepository.findById(id).orElseThrow( () -> new NotFoundException("Project not found"));

        DecodedJWT decodedToken = JWT.decode(token);
        boolean isOwner = decodedToken.getSubject().equals(project.getOwner().getId().toString());
        boolean isAdmin = decodedToken.getClaim("role").asString().equals("ADMIN");

        if (!(isOwner || isAdmin)) {
            throw new UnauthorizedException("You are not allowed to update this project.");
        }

        projectValidator.validate(project);

        if (project.getStartDate().isBefore(LocalDate.now())) {
            throw new ProjectAlreadyStartedException("You cannot update a project that has been already started.");
        }

        projectMapper.updateEntity(project, dto);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDTO(updatedProject);
    }

    public List<ProjectResponseDTO> findProjectsByOwner(UUID ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Owner not found"));
        return projectRepository.findByOwner(owner)
                .stream()
                .map(projectMapper::toDTO)
                .toList();
    }
}
