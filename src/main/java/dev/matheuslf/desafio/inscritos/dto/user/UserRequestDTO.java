package dev.matheuslf.desafio.inscritos.dto.user;

import dev.matheuslf.desafio.inscritos.annotation.StrongPassword;
import dev.matheuslf.desafio.inscritos.annotation.ValidRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Requisição de Usuário", description = "Dados necessários para criar um novo usuário")
public record UserRequestDTO(
        @Schema(name = "nome", description = "Nome do usuário", example = "João Silva")
        @NotBlank(message = "Name cannot be blank.")
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters.")
        String name,
        @Schema(name = "email", description = "Email do usuário", example = "joao.silva@exemplo.com")
        @NotBlank(message = "Email cannot be blank.")
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String email,
        @Schema(name = "senha", description = "Senha do usuário", example = "SenhaForte@123")
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, max = 20, message = "Password cannot be shorter than 8 characters or longer than 100 characters.")
        @StrongPassword
        String password,
        @Schema(name = "funcao", description = "Funçao do usuário no sistema", example = "USER")
        @ValidRole
        String role
) {
}
