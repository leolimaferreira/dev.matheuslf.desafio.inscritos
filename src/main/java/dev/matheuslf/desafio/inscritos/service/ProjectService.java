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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private static final String PROJECT_NOT_FOUND= "Project not found.";
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskService taskService;
    private final ProjectValidator projectValidator;
    private final UserRepository userRepository;

    public ProjectResponseDTO saveProject(ProjectRequestDTO dto) {
        Project project = projectMapper.toEntity(dto);
        projectValidator.validate(project);

        if (dto.endDate().isBefore(project.getStartDate())) {
            throw new InvalidProjectDatesException("Project's start date must be before the end date.");
        }

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    public Page<ProjectResponseDTO> findAllProjects(Integer page, Integer size) {
        Pageable pageRequest = Pageable.ofSize(size).withPage(page);
        Page<Project> projects = projectRepository.findAll(pageRequest);
        return projects.map(projectMapper::toDTO);
    }

    public void deleteProject(String token, UUID id) {
        Project project = projectRepository.findById(id).orElseThrow( () -> new NotFoundException(PROJECT_NOT_FOUND));

        if (isOwnerOrAdmin(token, project)) {
            throw new UnauthorizedException("You are not allowed to update this project.");
        }

        if (taskService.hasDoingTasks(project)) {
            throw new ProjectWithActiveTasksException("There are tasks with status DOING in this project");
        }
        projectRepository.delete(project);
        projectMapper.toDTO(project);
    }

    public void updateProject(String token, UUID id, UpdateProjectDTO dto) {
        Project project = projectRepository.findById(id).orElseThrow( () -> new NotFoundException(PROJECT_NOT_FOUND));

        if (isOwnerOrAdmin(token, project)) {
            throw new UnauthorizedException("You are not allowed to update this project.");
        }

        projectValidator.validate(project);

        projectMapper.updateEntity(project, dto);
        projectRepository.save(project);
    }

    public List<ProjectResponseDTO> findByUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return projectRepository.findByOwnerOrAssignee(user)
                .stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO findProjectById(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow( () -> new NotFoundException(PROJECT_NOT_FOUND));
        return projectMapper.toDTO(project);
    }

    private boolean isOwnerOrAdmin(String token, Project project) {
        DecodedJWT decodedToken = JWT.decode(token);
        return !decodedToken.getClaim("role").asString().equals("ADMIN") && !decodedToken.getSubject().equals(project.getOwner().getId().toString());
    }
}
