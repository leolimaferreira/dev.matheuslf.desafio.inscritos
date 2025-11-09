package dev.matheuslf.desafio.inscritos.exception;

public class ProjectAlreadyStartedException extends RuntimeException {
    public ProjectAlreadyStartedException(String message) {
        super(message);
    }
}
