package dev.matheuslf.desafio.inscritos.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Requisição de Login", description = "Dados necessários para autenticação do usuário")
public record LoginRequestDTO (
        @Schema(name = "email", description = "Email do usuário", example = "usuario@exemplo.com")
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String email,
        @Schema(name = "senha", description = "Senha do usuário", example = "Senha@123")
        @NotBlank(message = "Password cannot be blank.")
        String password
){
}
