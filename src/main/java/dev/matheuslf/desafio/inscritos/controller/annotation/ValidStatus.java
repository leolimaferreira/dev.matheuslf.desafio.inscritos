package dev.matheuslf.desafio.inscritos.controller.annotation;

import dev.matheuslf.desafio.inscritos.validator.StatusValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StatusValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatus {
    String message() default "Invalid Status";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};
}