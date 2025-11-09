package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.login.LoginRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.login.LoginResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.PasswordRecoveryToken;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.mapper.PasswordRecoveryTokenMapper;
import dev.matheuslf.desafio.inscritos.repository.PasswordRecoveryTokenRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import dev.matheuslf.desafio.inscritos.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    private final PasswordRecoveryTokenMapper passwordRecoveryTokenMapper;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        log.info("Login request received with email: {}", dto.email());

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", dto.email());
                    return new NotFoundException("User not found with email: " + dto.email());
                });

        log.info("User found: {}, role: {}", user.getName(), user.getRole());

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            log.warn("Invalid password for user: {}", dto.email());
            throw new BadCredentialsException("Invalid credentials");
        }

        log.info("Password validated successfully for user: {}", dto.email());

        String token = tokenService.generateToken(user);
        log.info("Token generated successfully for user: {}", dto.email());

        return new LoginResponseDTO(token, user.getName());
    }

    public RecoveryResponseDTO generateRecoveryToken(RecoveryRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new NotFoundException("User not found with email"));
        PasswordRecoveryToken recoveryToken = new PasswordRecoveryToken();
        recoveryToken.setUser(user);
        PasswordRecoveryToken savedToken = passwordRecoveryTokenRepository.save(recoveryToken);
        return passwordRecoveryTokenMapper.toDTO(savedToken);
    }
}
