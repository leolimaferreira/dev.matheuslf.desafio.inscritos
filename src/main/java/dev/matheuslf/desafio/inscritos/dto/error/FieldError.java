package dev.matheuslf.desafio.inscritos.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Erro de Campo", description = "Representa um erro de validação em um campo específico")
public record FieldError(
        @Schema(name = "campo", description = "Nome do campo que contém o erro", example = "email")
        String field,
        @Schema(name = "erro", description = "Mensagem de erro do campo", example = "O email não pode estar vazio")
        String error
) {
}
