package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.annotation.ValidPriority;
import dev.matheuslf.desafio.inscritos.annotation.ValidStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(name = "Atualização de Tarefa", description = "Dados para atualizar uma tarefa existente")
public record UpdateTaskDTO(
        @Schema(name = "titulo", description = "Título da tarefa", example = "Implementar funcionalidade de login - Atualizado")
        @Size(min = 5, max = 150, message = "Title cannot be shorter than 5 characters or longer than 150 characters.")
        String title,
        @Schema(name = "descricao", description = "Descrição da tarefa", example = "Desenvolver a tela de login com validação de credenciais e recuperação de senha")
        @Size(max = 255, message = "Description cannot be longer than 255 characters.")
        String description,
        @Schema(name = "status", description = "Status da tarefa", example = "TODO")
        @ValidStatus
        String status,
        @Schema(name = "prioridade", description = "Prioridade da tarefa", example = "MEDIUM")
        @ValidPriority
        String priority,
        @Schema(name = "dataVencimento", description = "Data de vencimento da tarefa", example = "2026-01-15")
        @FutureOrPresent(message = "Due date must be in the present or future.")
        LocalDate dueDate,
        @Schema(name = "emailResponsavel", description = "Email do usuário responsável pela tarefa", example = "novoresponsavel@exemplo.com")
        String assigneeEmail
) {
}
