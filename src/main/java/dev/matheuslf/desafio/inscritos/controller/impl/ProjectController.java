package dev.matheuslf.desafio.inscritos.controller.impl;

import dev.matheuslf.desafio.inscritos.controller.GenericController;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.project.UpdateProjectDTO;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController implements GenericController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> save(@RequestBody @Valid ProjectRequestDTO dto) {
        return ResponseEntity.ok(projectService.saveProject(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponseDTO>> findAll(@RequestParam(value = "page", defaultValue = "0")
                                                            Integer page,
                                                            @RequestParam(value = "size", defaultValue = "10")
                                                            Integer size) {
        return ResponseEntity.ok(projectService.findAllProjects(page, size));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ProjectResponseDTO>> findByOwner(@PathVariable(value = "ownerId") UUID ownerId) {
        return ResponseEntity.ok(projectService.findProjectsByOwner(ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> findById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(projectService.findProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(
            @RequestHeader(value = "Authorization")
            String token,
            @PathVariable(value = "id")
            UUID id,
            @RequestBody @Valid
            UpdateProjectDTO dto) {
        return ResponseEntity.ok(projectService.updateProject(token, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
