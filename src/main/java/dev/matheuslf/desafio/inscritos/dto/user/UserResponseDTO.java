package dev.matheuslf.desafio.inscritos.dto.user;

import dev.matheuslf.desafio.inscritos.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "Resposta de Usuário", description = "Dados de um usuário")
public record UserResponseDTO(
        @Schema(name = "id", description = "Identificador único do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(name = "nome", description = "Nome do usuário", example = "João Silva")
        String name,
        @Schema(name = "email", description = "Email do usuário", example = "joao.silva@exemplo.com")
        String email,
        @Schema(name = "funcao", description = "Funçao do usuário no sistema", example = "USER")
        Role role
) {
}
