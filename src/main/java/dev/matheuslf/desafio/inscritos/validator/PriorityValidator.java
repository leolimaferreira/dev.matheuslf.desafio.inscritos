package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.controller.annotation.ValidPriority;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PriorityValidator implements ConstraintValidator<ValidPriority, String> {
    private static final Set<String> VALID_PRIORITIES = Set.of("LOW", "MEDIUM", "HIGH");

    @Override
    public boolean isValid(String priority, ConstraintValidatorContext constraintValidatorContext) {
        if (priority == null) return true;
        return VALID_PRIORITIES.contains(priority.toUpperCase());
    }
}
