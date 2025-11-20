package dev.matheuslf.desafio.inscritos.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(name = "Requisição de Projeto", description = "Dados necessários para criar um novo projeto")
public record ProjectRequestDTO(
        @Schema(name = "nome", description = "Nome do projeto", example = "Sistema de Gestão")
        @NotBlank
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters.")
                String name,
        @Schema(name = "descricao", description = "Descrição do projeto", example = "Sistema para gerenciar tarefas e projetos")
        @Size(max = 255, message = "Description cannot be longer than 255 characters.")
        String description,
        @Schema(name = "dataInicio", description = "Data de início do projeto", example = "2025-01-01")
        @FutureOrPresent(message = "Start date must be in the present or future")
        LocalDate startDate,
        @Schema(name = "dataFim", description = "Data de término do projeto", example = "2025-12-31")
        @Future(message = "End date must be in the future")
        LocalDate endDate,
        @Schema(name = "emailProprietario", description = "Email do proprietário do projeto", example = "proprietario@exemplo.com")
        @NotBlank(message = "Email cannot be blank.")
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String ownerEmail
) {
}
