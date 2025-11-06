package dev.matheuslf.desafio.inscritos.exception.handler;

import dev.matheuslf.desafio.inscritos.dto.error.ResponseError;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<dev.matheuslf.desafio.inscritos.dto.error.FieldError> errorsList = fieldErrors.stream().map(fe -> new dev.matheuslf.desafio.inscritos.dto.error.FieldError(fe.getField(), fe.getDefaultMessage())).toList();
        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error.", errorsList);
    }

}
