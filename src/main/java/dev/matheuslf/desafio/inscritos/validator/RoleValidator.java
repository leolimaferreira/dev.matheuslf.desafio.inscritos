package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.annotation.ValidRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    private static final Set<String> VALID_ROLES = Set.of("USER", "ADMIN");

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        if (role == null) return true;
        return VALID_ROLES.contains(role.toUpperCase());
    }
}
