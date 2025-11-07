package dev.matheuslf.desafio.inscritos.repository.specs;

import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecs {

    private TaskSpecs() {
    }

    public static Specification<Task> titleLike(String title) {
        return (root, query, cb) -> cb.like( cb.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<Task> descriptionLike(String description) {
        return (root, query, cb) -> cb.like( cb.upper(root.get("description")), "%" + description.toUpperCase() + "%");
    }

    public static Specification<Task> statusEqual(Status status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Task> priorityEqual(Priority priority) {
        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> projectNameLike(String projectName) {
        return (root, query, cb) -> {
            Join<Object, Object> joinProject = root.join("project", JoinType.INNER);
            return cb.like(cb.upper(joinProject.get("name")), "%" + projectName.toUpperCase() + "%");
        };
    }
}
