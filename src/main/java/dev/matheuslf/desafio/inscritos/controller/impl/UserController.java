package dev.matheuslf.desafio.inscritos.controller.impl;

import dev.matheuslf.desafio.inscritos.controller.GenericController;
import dev.matheuslf.desafio.inscritos.dto.user.UpdateUserDTO;
import dev.matheuslf.desafio.inscritos.dto.user.UserRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO;
import dev.matheuslf.desafio.inscritos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements GenericController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Criaçao de usuários", description = "Registra um novo usuário")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    @ApiResponse(responseCode = "409", description = "Usuário com o mesmo email já existe.")
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO response = userService.saveUser(dto);
        URI location = generateHeaderLocation(response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/emails")
    @Operation(summary = "Lista de emails dos usuários", description = "Retorna a lista dos emails de todos os usuários")
    @ApiResponse(responseCode = "200", description = "Lista de emails retornada com sucesso.")
    public ResponseEntity<List<String>> findAllEmails() {
        return ResponseEntity.ok(userService.findAllEmails());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizaçao de usuários", description = "Atualiza um usuário")
    @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Usuário sem autorizaçao para atualizar usuário.")
    @ApiResponse(responseCode = "404", description = "Usuário nao encontrado.")
    public ResponseEntity<UserResponseDTO> update(
            @RequestHeader(value = "Authorization")
            String token,
            @PathVariable(value = "id")
            UUID id,
            @RequestBody @Valid
            UpdateUserDTO dto) {
        userService.updateUser(token, id, dto);
        return ResponseEntity.noContent().build();
    }
}
