package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.PasswordRecoveryToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordRecoveryTokenMapper {

    private final UserMapper userMapper;

    public RecoveryResponseDTO toDTO(PasswordRecoveryToken token) {
        return new RecoveryResponseDTO(
                token.getId(),
                token.getExpirationDate(),
                userMapper.toDTO(token.getUser())
        );
    }
}
