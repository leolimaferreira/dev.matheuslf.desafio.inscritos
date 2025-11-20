package dev.matheuslf.desafio.inscritos.controller.impl;

import dev.matheuslf.desafio.inscritos.controller.GenericController;
import dev.matheuslf.desafio.inscritos.dto.login.LoginRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.login.LoginResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.ChangePasswordDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.recovery.RecoveryResponseDTO;
import dev.matheuslf.desafio.inscritos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
@Tag(name = "Autorizaçao")
public class AuthController implements GenericController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Fazer login no sistema")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @ApiResponse(responseCode = "401", description = "Usuário nao encontrado.")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/generate-recovery-token")
    @Operation(summary = "Recuperaçao de senhas", description = "Enviar email para recuperar senha")
    @ApiResponse(responseCode = "200", description = "Email enviado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Erro ao enviar email.")
    @ApiResponse(responseCode = "404", description = "Usuário nao encontrado.")
    public ResponseEntity<RecoveryResponseDTO> generateRecoveryToken(@RequestBody RecoveryRequestDTO dto) throws MessagingException {
        return ResponseEntity.ok(authService.generateRecoveryToken(dto));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Atualizaçao de senhas", description = "Atualizar senha do usuário")
    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Nova senha nao pode ser igual a atual.")
    @ApiResponse(responseCode = "400", description = "Token de recuperaçao de senha expirado.")
    @ApiResponse(responseCode = "404", description = "Token de recuperaçao de senha nao encontrado.")
    @ApiResponse(responseCode = "404", description = "Usuário nao encontrado.")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        authService.changePassword(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/oauth2/google")
    @Operation(summary = "Login com Google", description = "Fazer login social no sistema via Google")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso.")
    public ResponseEntity<Map<String, String>> initiateGoogleLogin() {
        String authorizationUrl = "/oauth2/authorization/google";
        return ResponseEntity.ok(Map.of("redirectUrl", authorizationUrl));
    }
}
