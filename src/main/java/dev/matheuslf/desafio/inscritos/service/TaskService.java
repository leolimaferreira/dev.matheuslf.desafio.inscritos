package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.UpdateTaskDTO;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import dev.matheuslf.desafio.inscritos.validator.TaskValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static dev.matheuslf.desafio.inscritos.repository.specs.TaskSpecs.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final String TASK_NOT_FOUND_MESSAGE = "Task not found";
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskValidator taskValidator;
    private final ProjectRepository projectRepository;

    public TaskResponseDTO saveTask(TaskRequestDTO dto) {
        boolean priorityHigh = dto.priority().equals(Priority.HIGH.toString());
        Task task = taskMapper.toEntity(dto);

        taskValidator.validateTaskName(task);
        taskValidator.validateProjectEndDate(task.getProject());
        taskValidator.validateTaskDueDate(task.getDueDate(), task.getProject());

        if (priorityHigh) {
            taskValidator.validateNumberOfHighTasks(task.getProject());
            taskValidator.validateDescription(dto.description());
        }

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public TaskResponseDTO updateTask(UUID id, UpdateTaskDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow( () -> new NotFoundException(TASK_NOT_FOUND_MESSAGE));
        boolean priorityHigh = dto.priority() != null && dto.priority().equals(Priority.HIGH.toString());
        boolean updateToDone = dto.status() != null && dto.status().equals(Status.DONE.toString());
        boolean updateToDoing = dto.status() != null && dto.status().equals(Status.DOING.toString());
        Status taskStatus = task.getStatus();

        taskValidator.validateTaskName(task);
        taskValidator.validateProjectEndDate(task.getProject());
        taskValidator.validateTaskDueDate(task.getDueDate(), task.getProject());

        if (priorityHigh) {
            taskValidator.validateNumberOfHighTasks(task.getProject());
            taskValidator.validateDescription(dto.description());
        }

        if (updateToDone) {
            taskValidator.validateStatusChange(Status.DONE, taskStatus);
            taskValidator.validateTimeExpendedWithTask(task.getUpdatedAt());
        }

        if (updateToDoing) {
            taskValidator.validateStatusChange(Status.DOING, taskStatus);
        }

        taskMapper.updateEntity(task, dto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public void deleteTask(UUID id) {
        Task entity = taskRepository.findById(id).orElseThrow( () -> new NotFoundException(TASK_NOT_FOUND_MESSAGE));
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

    public List<TaskResponseDTO> findTasksByProject(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        return taskRepository.findAllByProject(project).stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public TaskResponseDTO findTaskById(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND_MESSAGE));
        return taskMapper.toDTO(task);
    }
}
