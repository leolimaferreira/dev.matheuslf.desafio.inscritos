package dev.matheuslf.desafio.inscritos.controller.impl;

import dev.matheuslf.desafio.inscritos.controller.GenericController;
import dev.matheuslf.desafio.inscritos.dto.login.LoginRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.login.LoginResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import dev.matheuslf.desafio.inscritos.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController implements GenericController {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        log.info("Login request received with email: {}", dto.email());

        try {
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

            return ResponseEntity.ok(new LoginResponseDTO(token, user.getName()));

        } catch (NotFoundException | BadCredentialsException e) {
            log.error("Authentication failed for email: {} - Reason: {}", dto.email(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during login for email: {} - Error: {}", dto.email(), e.getMessage(), e);
            throw new RuntimeException("Login process failed", e);
        }
    }
}
