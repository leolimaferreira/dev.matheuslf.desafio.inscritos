package dev.matheuslf.desafio.inscritos.dto.recovery;

import dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Resposta de Recuperação", description = "Dados do token de recuperação de senha gerado")
public record RecoveryResponseDTO(
        @Schema(name = "id", description = "Identificador único do token de recuperação", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(name = "dataExpiracao", description = "Data e hora de expiração do token", example = "2025-11-20T23:59:59")
        LocalDateTime expirationDate,
        @Schema(name = "usuario", description = "Dados do usuário que solicitou a recuperação")
        UserResponseDTO user
) {
}
