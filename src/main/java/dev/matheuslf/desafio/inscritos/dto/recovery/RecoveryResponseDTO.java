package dev.matheuslf.desafio.inscritos.dto.recovery;

import dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecoveryResponseDTO(
        UUID id,
        LocalDateTime expirationDate,
        UserResponseDTO user
) {
}
