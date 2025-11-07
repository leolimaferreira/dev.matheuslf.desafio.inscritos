package dev.matheuslf.desafio.inscritos.exception;

public class ProjectWithActiveTasksException extends RuntimeException {
    public ProjectWithActiveTasksException(String message) {
        super(message);
    }
}
