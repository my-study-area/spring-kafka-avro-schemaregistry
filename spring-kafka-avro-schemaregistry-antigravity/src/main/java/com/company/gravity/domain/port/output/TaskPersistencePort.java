package com.company.gravity.domain.port.output;

import com.company.gravity.domain.model.Task;
import java.util.Optional;
import java.util.UUID;

public interface TaskPersistencePort {
    void save(Task task);
    Optional<Task> findById(UUID id);
}
