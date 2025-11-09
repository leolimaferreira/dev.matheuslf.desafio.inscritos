package dev.matheuslf.desafio.inscritos.controller.impl;

import dev.matheuslf.desafio.inscritos.controller.GenericController;
import dev.matheuslf.desafio.inscritos.dto.login.LoginRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.login.LoginResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.ChangePasswordDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryResponseDTO;
import dev.matheuslf.desafio.inscritos.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController implements GenericController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/generate-recovery-token")
    public ResponseEntity<RecoveryResponseDTO> generateRecoveryToken(@RequestBody RecoveryRequestDTO dto) {
        return ResponseEntity.ok(authService.generateRecoveryToken(dto));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        authService.changePassword(dto);
        return ResponseEntity.noContent().build();
    }
}
