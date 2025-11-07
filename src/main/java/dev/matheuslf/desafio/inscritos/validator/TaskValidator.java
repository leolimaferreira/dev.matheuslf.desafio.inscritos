package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.exception.DescriptionNeededException;
import dev.matheuslf.desafio.inscritos.exception.InvalidTaskDueDateException;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
}
