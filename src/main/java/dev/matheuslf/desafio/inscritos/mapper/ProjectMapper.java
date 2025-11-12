package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.InvalidProjectDatesException;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ProjectResponseDTO toDTO(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                userMapper.toDTO(project.getOwner())
        );
    }

    public Project toEntity(ProjectRequestDTO dto) {
        User owner = userRepository.findByEmail(dto.ownerEmail()).orElseThrow( () -> new NotFoundException("Owner not found"));
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        project.setOwner(owner);
        return project;
    }

    public void updateEntity(Project project, UpdateProjectDTO dto) {
        if (dto.name() != null) {
            project.setName(dto.name());
        }

        if (dto.description() != null) {
            project.setDescription(dto.description());
        }

        if (dto.startDate() != null) {
            if (dto.endDate().isBefore(dto.startDate())) {
                throw new InvalidProjectDatesException("Project's start date must be before the end date.");
            }
            project.setStartDate(dto.startDate());
        }

        if (dto.endDate() != null) {
            if (dto.endDate().isBefore(dto.startDate())) {
                throw new InvalidProjectDatesException("Project's start date must be before the end date.");
            }
            project.setEndDate(dto.endDate());
        }

        if (dto.ownerEmail() != null) {
            User owner = userRepository.findByEmail(dto.ownerEmail()).orElseThrow( () -> new NotFoundException("Owner not found"));
            project.setOwner(owner);
        }
    }
}
