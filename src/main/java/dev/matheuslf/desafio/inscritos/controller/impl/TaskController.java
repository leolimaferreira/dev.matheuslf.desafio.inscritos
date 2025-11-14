package dev.matheuslf.desafio.inscritos.controller.impl;

import dev.matheuslf.desafio.inscritos.controller.GenericController;
import dev.matheuslf.desafio.inscritos.dto.task.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.task.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dto.task.UpdateTaskDTO;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.service.TaskService;
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
@RequestMapping("/tasks")
public class TaskController implements GenericController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> save(@RequestBody @Valid TaskRequestDTO dto) {
        TaskResponseDTO response = taskService.saveTask(dto);
        URI uri = generateHeaderLocation(response.id());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable("id") UUID id, @RequestBody @Valid UpdateTaskDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    @GetMapping("project/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> findByProject(@PathVariable("projectId") UUID projectId) {
        return ResponseEntity.ok(taskService.findTasksByProject(projectId));
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> findWithParams(
            @RequestParam(value = "title", required = false)
            String title,
            @RequestParam(value = "description", required = false)
            String description,
            @RequestParam(value = "status", required = false)
            Status status,
            @RequestParam(value = "priority", required = false)
            Priority priority,
            @RequestParam(value = "projectName", required = false)
            String projectName,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "size", defaultValue = "10")
            Integer size
    ) {
        return ResponseEntity.ok(taskService.findTasksWithParams(title, description, status, priority, projectName, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(taskService.findTaskById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
