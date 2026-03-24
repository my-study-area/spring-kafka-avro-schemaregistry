package com.company.gravity.application.service;

import com.company.gravity.application.usecase.CreateTaskUseCase;
import com.company.gravity.domain.event.EventEnvelope;
import com.company.gravity.domain.event.TaskProcessedEvent;
import com.company.gravity.domain.model.ProcessingStatus;
import com.company.gravity.domain.model.Task;
import com.company.gravity.domain.port.output.TaskPersistencePort;
import com.company.gravity.domain.port.output.TaskResponseOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService implements CreateTaskUseCase {

    private final TaskPersistencePort taskPersistencePort;
    private final TaskResponseOutputPort taskResponseOutputPort;

    @Override
    public Task create(String title, String description, String correlationId) {
        log.info("Iniciando criação de task com CorrelationId: {}", correlationId);

        try {
            // 1. Criar entidade no domínio
            Task task = Task.create(title, description);

            // 2. Persistir no banco H2 via Porta
            taskPersistencePort.save(task);

            log.info("Task salva no H2 com sucesso. Id: {}", task.getId());

            // 3. Enviar feedback de sucesso
            sendFeedback(task.getId(), title, ProcessingStatus.SUCCESS, null, correlationId);

            return task;
        } catch (Exception e) {
            log.error("Erro ao processar criação de task. CorrelationId: {}", correlationId, e);
            
            // Enviar feedback de falha
            sendFeedback(null, title, ProcessingStatus.FAILED, e.getMessage(), correlationId);
            
            throw e;
        }
    }

    private void sendFeedback(java.util.UUID taskId, String title, ProcessingStatus status, String reason, String correlationId) {
        TaskProcessedEvent event = TaskProcessedEvent.builder()
                .taskId(taskId)
                .title(title)
                .status(status)
                .reason(reason)
                .build();

        taskResponseOutputPort.sendFeedback(EventEnvelope.wrap(event, correlationId));
    }
}
