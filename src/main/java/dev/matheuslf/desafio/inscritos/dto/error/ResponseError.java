package dev.matheuslf.desafio.inscritos.dto.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseError(
        int status,
        String message,
        List<FieldError> errors
) {

    public static ResponseError defaultResponse(String message) {
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ResponseError conflict(String mensagem) {
        return new ResponseError(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }

    public static ResponseError unprocessbleEntity(String mensagem) {
        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), mensagem, List.of());
    }

    public static ResponseError of(HttpStatus status, String message, List<FieldError> errors) {
        return new ResponseError(status.value(), message, errors);
    }
}
