package dev.matheuslf.desafio.inscritos.controller.impl;

import dev.matheuslf.desafio.inscritos.controller.GenericController;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController implements GenericController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Criaçao de projetos", description = "Registra um novo projeto")
    @ApiResponse(responseCode = "201", description = "Projeto registrado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    @ApiResponse(responseCode = "404", description = "Dono do projeto nao encontrado.")
    @ApiResponse(responseCode = "409", description = "Projeto com o mesmo nome já existe.")
    public ResponseEntity<ProjectResponseDTO> save(@RequestBody @Valid ProjectRequestDTO dto) {
        ProjectResponseDTO saveProject = projectService.saveProject(dto);
        URI uri = generateHeaderLocation(saveProject.id());
        return ResponseEntity.created(uri).body(saveProject);
    }

    @GetMapping
    @Operation(summary = "Páginas de projetos", description = "Retorna a páginas de projetos")
    @ApiResponse(responseCode = "200", description = "Páginas retornadas com sucesso.")
    public ResponseEntity<Page<ProjectResponseDTO>> findAll(@RequestParam(value = "page", defaultValue = "0")
                                                            Integer page,
                                                            @RequestParam(value = "size", defaultValue = "10")
                                                            Integer size) {
        return ResponseEntity.ok(projectService.findAllProjects(page, size));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lista de projetos", description = "Retorna a lista de projetos que o usuário faz parte")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Usuário nao encontrado.")
    public ResponseEntity<List<ProjectResponseDTO>> findByUser(@PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.ok(projectService.findByUser(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca de projeto", description = "Retorna um projeto com base no id passado")
    @ApiResponse(responseCode = "200", description = "Projeto retornado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Projeto nao encontrado.")
    public ResponseEntity<ProjectResponseDTO> findById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(projectService.findProjectById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizaçao de projeto", description = "Atualiza um projeto parcialmente")
    @ApiResponse(responseCode = "204", description = "Projeto atualizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Novos dados inválidos.")
    @ApiResponse(responseCode = "401", description = "Usuário sem autorizaçao para atualizar o projeto.")
    @ApiResponse(responseCode = "404", description = "Projeto nao encontrado.")
    @ApiResponse(responseCode = "404", description = "Novo dono do projeto nao encontrado.")
    @ApiResponse(responseCode = "409", description = "Projeto com o novo nome já existente.")
    public ResponseEntity<ProjectResponseDTO> update(
            @RequestHeader(value = "Authorization")
            String token,
            @PathVariable(value = "id")
            UUID id,
            @RequestBody @Valid
            UpdateProjectDTO dto) {
        projectService.updateProject(token, id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleçao de projeto", description = "Deleta um projeto")
    @ApiResponse(responseCode = "204", description = "Projeto deletado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Usuário sem autorizaçao para deletar o projeto.")
    @ApiResponse(responseCode = "404", description = "Projeto nao encontrado.")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "Authorization")
            String token,
            @PathVariable(value = "id")
            UUID id) {
        projectService.deleteProject(token, id);
        return ResponseEntity.noContent().build();
    }
}
