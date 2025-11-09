package dev.matheuslf.desafio.inscritos.dto.recovery;

import dev.matheuslf.desafio.inscritos.annotation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChangePasswordDTO(
        UUID tokenId,
        @NotBlank(message = "New password cannot be blank.")
        @Size(min = 8, max = 20, message = "New password cannot be shorter than 8 characters or longer than 20 characters.")
        @StrongPassword
        String newPassword
) {
}
