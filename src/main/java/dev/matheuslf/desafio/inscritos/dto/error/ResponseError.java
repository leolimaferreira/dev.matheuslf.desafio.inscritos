package dev.matheuslf.desafio.inscritos.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.List;

@Schema(name = "Resposta de Erro", description = "Representa uma resposta de erro")
public record ResponseError(
        @Schema(name = "status", description = "Status do erro", example = "404")
        int status,
        @Schema(name = "mensagem", description = "Mensagem de erro", example = "Usuário nao encontrado")
        String message,
        @Schema(name = "erros", description = "Nomes dos campos que contém os erros")
        List<FieldError> errors
) {

    public static ResponseError conflict(String mensagem) {
        return new ResponseError(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }

    public static ResponseError of(HttpStatus status, String message, List<FieldError> errors) {
        return new ResponseError(status.value(), message, errors);
    }
}
