package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.login.LoginRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.login.LoginResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.ChangePasswordDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.PasswordRecoveryToken;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.ExpiredRecoveryTokenException;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.exception.SamePasswordException;
import dev.matheuslf.desafio.inscritos.exception.UnauthorizedException;
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

    private static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    private final PasswordRecoveryTokenMapper passwordRecoveryTokenMapper;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        log.info("Login request received with email: {}", dto.email());

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> {
                    log.warn(INVALID_EMAIL_OR_PASSWORD);
                    return new UnauthorizedException(INVALID_EMAIL_OR_PASSWORD);
                });

        log.info("User found: {}, role: {}", user.getName(), user.getRole());

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            log.warn(INVALID_EMAIL_OR_PASSWORD);
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

    public void changePassword(ChangePasswordDTO dto) {
        PasswordRecoveryToken recoveryToken = passwordRecoveryTokenRepository.findById(dto.tokenId()).orElseThrow(() -> new NotFoundException("Recovery token not found"));

        if (recoveryToken.getExpirationDate().isBefore(java.time.LocalDateTime.now())) {
            throw new ExpiredRecoveryTokenException("Password recovery token expired");
        }

        User user = userRepository.findById(recoveryToken.getUser().getId()).orElseThrow(() -> new NotFoundException("User not found"));

        if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
            throw new SamePasswordException("New password cannot be the same as the current one");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        recoveryToken.setUsed(true);
        passwordRecoveryTokenRepository.save(recoveryToken);
        userRepository.save(user);
    }
}
