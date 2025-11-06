package dev.matheuslf.desafio.inscritos.controller.impl;

import dev.matheuslf.desafio.inscritos.controller.GenericController;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
