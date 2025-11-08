package dev.matheuslf.desafio.inscritos.annotation;

import dev.matheuslf.desafio.inscritos.validator.StrongPasswordValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "Your password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter and one special character.";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
