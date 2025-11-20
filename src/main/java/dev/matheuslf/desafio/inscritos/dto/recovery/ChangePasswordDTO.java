package dev.matheuslf.desafio.inscritos.dto.recovery;

import dev.matheuslf.desafio.inscritos.annotation.StrongPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(name = "Alteração de Senha", description = "Dados necessários para alterar a senha do usuário")
public record ChangePasswordDTO(
        @Schema(name = "tokenId", description = "Identificador do token de recuperação", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID tokenId,
        @Schema(name = "novaSenha", description = "Nova senha do usuário", example = "NovaSenha@123")
        @NotBlank(message = "New password cannot be blank.")
        @Size(min = 8, max = 20, message = "New password cannot be shorter than 8 characters or longer than 20 characters.")
        @StrongPassword
        String newPassword
) {
}
