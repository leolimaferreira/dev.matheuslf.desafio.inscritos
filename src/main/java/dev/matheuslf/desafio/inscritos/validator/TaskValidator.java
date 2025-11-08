package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.DescriptionNeededException;
import dev.matheuslf.desafio.inscritos.exception.InvalidTaskDueDateException;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskValidator {

    private final TaskRepository taskRepository;

    public void validateDescription(String description) {
        if (description.isEmpty()) {
            throw new DescriptionNeededException("High priority tasks must have a description");
        }
    }

    public void validateNumberOfHighTasks(Project project) {
        if (taskRepository.countTaskByProjectAndPriority(project, Priority.HIGH) >= 5) {
            throw new DescriptionNeededException("Project cannot have more than 5 high priority tasks");
        }
    }

    public void validateProjectEndDate(Project project) {
        if (project.getEndDate().isBefore(LocalDate.now())) {
            throw new DescriptionNeededException("Project has already ended");
        }
    }

    public void validateTaskDueDate(LocalDate taskDueDate, Project project) {
        if (taskDueDate.isAfter(project.getEndDate())) {
            throw new InvalidTaskDueDateException("Task due date cannot be after project end date");
        }

        if (taskDueDate.isBefore(project.getStartDate())) {
            throw new InvalidTaskDueDateException("Task due date cannot be before project start date");
        }
    }

    public void validateTimeExpendedWithTask(LocalDateTime taskLastUpdate) {
        if (taskLastUpdate.plusMinutes(30).isAfter(LocalDateTime.now())) {
            throw new DescriptionNeededException("You cannot update a task to DONE less than 30 minutes after its creation");
        }
    }

    public void validateTaskName(Task task) {
        if (existsRegisteredTask(task)) {
            throw new ConflictException("There is already a task with this name in this project.");
        }
    }

    private boolean existsRegisteredTask(Task task) {
        Optional<Task> foundTask = taskRepository.findByTitleAndProject(task.getTitle(), task.getProject());

        if (task.getId() == null) {
            return foundTask.isPresent();
        }

        return foundTask.isPresent() && !task.getId().equals(foundTask.get().getId());
    }
}
