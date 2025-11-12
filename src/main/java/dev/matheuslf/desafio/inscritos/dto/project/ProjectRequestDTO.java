package dev.matheuslf.desafio.inscritos.dto.project;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProjectRequestDTO(
        @NotBlank
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters.")
                String name,
        @Size(max = 255, message = "Description cannot be longer than 255 characters.")
        String description,
        @FutureOrPresent(message = "Start date must be in the present or future")
        LocalDate startDate,
        @Future(message = "End date must be in the future")
        LocalDate endDate,
        String ownerEmail
) {
}
