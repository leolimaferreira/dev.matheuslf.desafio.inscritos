package dev.matheuslf.desafio.inscritos.exception;

import lombok.Getter;

public class InvalidFieldException extends RuntimeException {

    @Getter
    private final String field;

    public InvalidFieldException(String field, String message) {
        super(message);
        this.field = field;
    }
}
