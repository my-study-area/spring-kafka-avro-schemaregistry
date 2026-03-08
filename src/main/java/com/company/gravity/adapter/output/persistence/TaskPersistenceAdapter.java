package com.company.gravity.adapter.output.persistence;

import com.company.gravity.domain.model.Task;
import com.company.gravity.domain.port.output.TaskPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskPersistencePort {

    private final TaskJpaRepository taskJpaRepository;

    @Override
    public void save(Task task) {
        TaskJpaEntity entity = TaskJpaEntity.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .build();
        taskJpaRepository.save(entity);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return taskJpaRepository.findById(id)
                .map(entity -> Task.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .description(entity.getDescription())
                        .status(entity.getStatus())
                        .createdAt(entity.getCreatedAt())
                        .build());
    }
}

@Repository
interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, UUID> {
}
