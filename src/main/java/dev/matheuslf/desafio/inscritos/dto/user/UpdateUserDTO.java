package dev.matheuslf.desafio.inscritos.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters.")
        String name,
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String email
) {
}
