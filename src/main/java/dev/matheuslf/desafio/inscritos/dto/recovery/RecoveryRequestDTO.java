package dev.matheuslf.desafio.inscritos.dto.recovery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Requisição de Recuperação", description = "Dados necessários para solicitar recuperação de senha")
public record RecoveryRequestDTO (
        @Schema(name = "email", description = "Email do usuário que deseja recuperar a senha", example = "usuario@exemplo.com")
        @NotBlank(message = "Email cannot be blank.")
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String email
){
}
