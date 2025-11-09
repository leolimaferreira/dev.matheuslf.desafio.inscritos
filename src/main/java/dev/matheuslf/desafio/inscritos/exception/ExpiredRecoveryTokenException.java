package dev.matheuslf.desafio.inscritos.exception;

public class ExpiredRecoveryTokenException extends RuntimeException {
    public ExpiredRecoveryTokenException(String message) {
        super(message);
    }
}
