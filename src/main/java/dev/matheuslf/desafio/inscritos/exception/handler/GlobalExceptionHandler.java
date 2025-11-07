package dev.matheuslf.desafio.inscritos.exception.handler;

import dev.matheuslf.desafio.inscritos.dto.error.ResponseError;
import dev.matheuslf.desafio.inscritos.exception.ConflictException;
import dev.matheuslf.desafio.inscritos.exception.InvalidFieldException;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.exception.ProjectWithActiveTasksException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleNotFoundException(NotFoundException e) {
        log.error("Resource not found: {}", e.getMessage());
        return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleDuplicatedRegistryException(ConflictException e) {
        return ResponseError.conflict(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<dev.matheuslf.desafio.inscritos.dto.error.FieldError> errorsList = fieldErrors.stream().map(fe -> new dev.matheuslf.desafio.inscritos.dto.error.FieldError(fe.getField(), fe.getDefaultMessage())).toList();
        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error.", errorsList);
    }

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleInvalidFieldException(InvalidFieldException e) {
        return ResponseError.of(
                HttpStatus.BAD_REQUEST,
                "Validation error",
                List.of(new dev.matheuslf.desafio.inscritos.dto.error.FieldError(e.getField(), e.getMessage()))
        );
    }

    @ExceptionHandler(ProjectWithActiveTasksException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleProjectWithActiveTasksException(ProjectWithActiveTasksException e) {
        log.error("Cannot delete project: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Invalid request format: {}", e.getMessage());
        String message = "Invalid enum value provided";
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleGenericException() {
        return ResponseError.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                List.of()
        );
    }
}
