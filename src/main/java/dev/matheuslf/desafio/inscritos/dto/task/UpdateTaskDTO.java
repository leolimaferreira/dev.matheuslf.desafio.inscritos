package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTaskDTO(
        @Size(min = 5, max = 150, message = "Title cannot be shorter than 5 characters or longer than 150 characters.")
        String title,
        @Size(max = 255, message = "Description cannot be longer than 255 characters.")
        String description,
        Status status,
        Priority priority,
        @FutureOrPresent(message = "Due date must be in the present or future.")
        LocalDate dueDate
) {
}
