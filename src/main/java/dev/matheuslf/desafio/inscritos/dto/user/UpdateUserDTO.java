package dev.matheuslf.desafio.inscritos.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(name = "Atualização de Usuário", description = "Dados para atualizar um usuário existente")
public record UpdateUserDTO(
        @Schema(name = "nome", description = "Nome do usuário", example = "João Silva Atualizado")
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters.")
        String name,
        @Schema(name = "email", description = "Email do usuário", example = "novoemail@exemplo.com")
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String email
) {
}
