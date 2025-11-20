package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Resposta de Tarefa", description = "Dados de uma tarefa")
public record TaskResponseDTO(
        @Schema(name = "id", description = "Identificador único da tarefa", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(name = "titulo", description = "Título da tarefa", example = "Implementar funcionalidade de login")
        String title,
        @Schema(name = "descricao", description = "Descrição da tarefa", example = "Desenvolver a tela de login com validação de credenciais")
        String description,
        @Schema(name = "status", description = "Status da tarefa", example = "TODO")
        Status status,
        @Schema(name = "prioridade", description = "Prioridade da tarefa", example = "HIGH")
        Priority priority,
        @Schema(name = "responsavel", description = "Usuário responsável pela tarefa")
        dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO assignee,
        @Schema(name = "dataVencimento", description = "Data de vencimento da tarefa", example = "2025-12-31")
        LocalDate dueDate,
        @Schema(name = "projeto", description = "Projeto ao qual a tarefa pertence")
        ProjectResponseDTO project
) {
}
