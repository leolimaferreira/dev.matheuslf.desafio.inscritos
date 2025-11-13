package dev.matheuslf.desafio.inscritos.repository;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
    boolean existsByProjectAndStatus(Project project, Status status);
    Integer countTaskByProjectAndPriority(Project project, Priority priority);
    Optional<Task> findByTitleAndProject(String title, Project project);

    List<Task> findAllByProject(Project project);
}
