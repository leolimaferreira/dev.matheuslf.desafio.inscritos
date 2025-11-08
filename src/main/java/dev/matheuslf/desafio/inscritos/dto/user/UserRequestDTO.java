package dev.matheuslf.desafio.inscritos.dto.user;

import dev.matheuslf.desafio.inscritos.annotation.StrongPassword;
import dev.matheuslf.desafio.inscritos.annotation.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Name cannot be blank.")
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters.")
        String name,
        @NotBlank(message = "Email cannot be blank.")
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String email,
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, max = 20, message = "Password cannot be shorter than 8 characters or longer than 100 characters.")
        @StrongPassword
        String password,
        @ValidRole
        String role
) {
}
