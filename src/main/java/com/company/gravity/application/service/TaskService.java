package com.company.gravity.application.service;

import com.company.gravity.application.usecase.CreateTaskUseCase;
import com.company.gravity.domain.model.Task;
import com.company.gravity.domain.port.output.TaskPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService implements CreateTaskUseCase {

    private final TaskPersistencePort taskPersistencePort;

    @Override
    public Task create(String title, String description, String correlationId) {
        log.info("Iniciando criação de task com CorrelationId: {}", correlationId);

        // 1. Criar entidade no domínio
        Task task = Task.create(title, description);

        // 2. Persistir no banco H2 via Porta
        taskPersistencePort.save(task);

        log.info("Task salva no H2 com sucesso. Id: {}", task.getId());
        return task;
    }
}
