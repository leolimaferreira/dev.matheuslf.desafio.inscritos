package dev.matheuslf.desafio.inscritos.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Resposta de Login", description = "Dados retornados após autenticação bem-sucedida")
public record LoginResponseDTO(
        @Schema(name = "token", description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,
        @Schema(name = "nome", description = "Nome do usuário autenticado", example = "João Silva")
        String name
) {
}
