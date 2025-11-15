package dev.matheuslf.desafio.inscritos.exception.handler;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.matheuslf.desafio.inscritos.dto.error.ResponseError;
import dev.matheuslf.desafio.inscritos.exception.*;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(ProjectEndedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleProjectEndedException(ProjectEndedException e) {
        log.error("Cannot update task: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(DescriptionNeededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleDescriptionNeededException(DescriptionNeededException e) {
        log.error("Cannot create/update task: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(NumberOfHighTasksExceedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleNumberOfHighTasksExceedException(NumberOfHighTasksExceedException e) {
        log.error("Cannot create task: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(InvalidTaskDueDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleInvalidTaskDueDateException(InvalidTaskDueDateException e) {
        log.error("Invalid task due date: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Invalid request format: {}", e.getMessage());
        String message = "Invalid enum value provided";
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    @ExceptionHandler(InvalidTimeExpendedWithTaskException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleInvalidTimeExpendedWithTaskException(InvalidTimeExpendedWithTaskException e) {
        log.error("Invalid time expended with task: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(InvalidStatusChangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleInvalidStatusChangeException(InvalidStatusChangeException e) {
        log.error("Invalid status change in task: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handleBadCredentialsException(BadCredentialsException e) {
        log.error("Authentication failed: {}", e.getMessage());
        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(ExpiredRecoveryTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handleExpiredRecoveryTokenException(ExpiredRecoveryTokenException e) {
        log.error("Error while validating recovery token: {}", e.getMessage());
        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handleUnauthorizedException(UnauthorizedException e) {
        log.error("Error while doing user authorities verification: {}", e.getMessage());
        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(SamePasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleSamePasswordException(SamePasswordException e) {
        log.error("Error while changing password: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(InvalidProjectDatesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleInvalidProjectDatesException(InvalidProjectDatesException e) {
        log.error("Error while updating project dates: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(JWTCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleJWTCreationException(JWTCreationException e) {
        log.error("Error while authenticating: {}", e.getMessage());
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleSMessagingException(MessagingException e) {
        log.error("Error while sending recovery email: {}", e.getMessage());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleJWTVerificationException(JWTVerificationException e) {
        log.error("Error while doing the JWT verification: {}", e.getMessage());
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), List.of());
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
