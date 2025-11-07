package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.controller.annotation.ValidStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class StatusValidator implements ConstraintValidator<ValidStatus, String> {
    private static final Set<String> VALID_STATUS = Set.of("TODO", "DOING", "DONE");

    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {
        if (status == null) return true;
        return VALID_STATUS.contains(status.toUpperCase());
    }
}
