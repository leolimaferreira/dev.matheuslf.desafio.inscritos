package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.UpdateTaskDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.matheuslf.desafio.inscritos.repository.specs.TaskSpecs.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO saveTask(TaskRequestDTO dto) {
        Task entity = taskMapper.toEntity(dto);
        Task savedTask = taskRepository.save(entity);
        return taskMapper.toDTO(savedTask);
    }

    public TaskResponseDTO updateTask(UUID id, UpdateTaskDTO dto) {
        Task entity = taskRepository.findById(id).orElseThrow( () -> new RuntimeException("Task not found"));
        taskMapper.updateEntity(entity, dto);
        Task savedTask = taskRepository.save(entity);
        return taskMapper.toDTO(savedTask);
    }

    public void deleteTask(UUID id) {
        Task entity = taskRepository.findById(id).orElseThrow( () -> new RuntimeException("Task not found"));
        taskRepository.delete(entity);
        taskMapper.toDTO(entity);
    }

    public Page<TaskResponseDTO> findTasksWithParams(String title, String description, Status status, Priority priority, String projectName, Integer page, Integer size) {
        Specification<Task> specs = null;

        if (title != null) {
            specs = titleLike(title);
        }

        if (description != null) {
            specs = (specs == null) ? descriptionLike(description) : specs.and(descriptionLike(description));
        }

        if (status != null) {
            specs = (specs == null) ? statusEqual(status) : specs.and(statusEqual(status));
        }

        if (priority != null) {
            specs = (specs == null) ? priorityEqual(priority) : specs.and(priorityEqual(priority));
        }

        if (projectName != null) {
            specs = (specs == null) ? projectNameLike(projectName) : specs.and(projectNameLike(projectName));
        }

        Pageable pageRequest = Pageable.ofSize(size).withPage(page);

        Page<Task> tasks = taskRepository.findAll(specs, pageRequest);

        return tasks.map(taskMapper::toDTO);
    }

    public boolean hasDoingTasks(Project project) {
        return taskRepository.existsByProjectAndStatus(project, Status.DOING);
    }
}
