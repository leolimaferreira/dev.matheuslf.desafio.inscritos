package dev.matheuslf.desafio.inscritos.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO (
        @Email
        @Size(max = 150, message = "Email cannot be longer than 150 characters.")
        String email,
        @NotBlank(message = "Password cannot be blank.")
        String password
){
}
