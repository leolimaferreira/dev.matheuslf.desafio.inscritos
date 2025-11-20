package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.annotation.ValidPriority;
import dev.matheuslf.desafio.inscritos.annotation.ValidStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(name = "Requisição de Tarefa", description = "Dados necessários para criar uma nova tarefa")
public record TaskRequestDTO(
        @Schema(name = "titulo", description = "Título da tarefa", example = "Implementar funcionalidade de login")
        @NotBlank
        @Size(min = 5, max = 150, message = "Title cannot be shorter than 5 characters or longer than 150 characters.")
        String title,
        @Schema(name = "descricao", description = "Descrição da tarefa", example = "Desenvolver a tela de login com validação de credenciais")
        @Size(max = 255, message = "Description cannot be longer than 255 characters.")
        String description,
        @Schema(name = "status", description = "Status da tarefa", example = "TODO")
        @ValidStatus
        String status,
        @Schema(name = "prioridade", description = "Prioridade da tarefa", example = "HIGH")
        @ValidPriority
        String priority,
        @Schema(name = "dataVencimento", description = "Data de vencimento da tarefa", example = "2025-12-31")
        @FutureOrPresent(message = "Due date must be in the present or future.")
        LocalDate dueDate,
        @Schema(name = "nomeProjeto", description = "Nome do projeto ao qual a tarefa pertence", example = "Sistema de Gestão")
        @NotBlank
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters.")
        String projectName,
        @Schema(name = "emailResponsavel", description = "Email do usuário responsável pela tarefa", example = "responsavel@exemplo.com")
        @NotBlank(message = "Email cannot be blank.")
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String assigneeEmail
) {
}
