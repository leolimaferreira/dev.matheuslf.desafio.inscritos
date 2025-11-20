package dev.matheuslf.desafio.inscritos.dto.project;

import dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(name = "Resposta de Projeto", description = "Dados de um projeto")
public record ProjectResponseDTO(
        @Schema(name = "id", description = "Identificador único do projeto", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(name = "nome", description = "Nome do projeto", example = "Sistema de Gestão")
        String name,
        @Schema(name = "descricao", description = "Descrição do projeto", example = "Sistema para gerenciar tarefas e projetos")
        String description,
        @Schema(name = "dataInicio", description = "Data de início do projeto", example = "2025-01-01")
        LocalDate startDate,
        @Schema(name = "dataFim", description = "Data de término do projeto", example = "2025-12-31")
        LocalDate endDate,
        @Schema(name = "proprietario", description = "Proprietário do projeto")
        UserResponseDTO owner,
        @Schema(name = "participantes", description = "Lista de usuários atribuídos ao projeto")
        List<UserResponseDTO> assignees
) {
}
