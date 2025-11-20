package dev.matheuslf.desafio.inscritos.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(name = "Atualização de Projeto", description = "Dados para atualizar um projeto existente")
public record UpdateProjectDTO(
        @Schema(name = "nome", description = "Nome do projeto", example = "Sistema de Gestão Atualizado")
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters.")
        String name,
        @Schema(name = "descricao", description = "Descrição do projeto", example = "Sistema para gerenciar tarefas e projetos - versão atualizada")
        @Size(max = 255, message = "Description cannot be longer than 255 characters.")
        String description,
        @Schema(name = "dataInicio", description = "Data de início do projeto", example = "2025-02-01")
        @FutureOrPresent(message = "Start date must be in the present or future")
        LocalDate startDate,
        @Schema(name = "dataFim", description = "Data de término do projeto", example = "2026-01-31")
        @Future(message = "End date must be in the future")
        LocalDate endDate,
        @Schema(name = "emailProprietario", description = "Email do proprietário do projeto", example = "novoproprietario@exemplo.com")
        String ownerEmail
) {
}
