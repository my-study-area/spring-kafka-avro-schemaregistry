package com.company.gravity.domain.event;

import com.company.gravity.domain.model.ProcessingStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Evento de retorno contendo o resultado do processamento da tarefa.
 */
@Getter
@Builder
public class TaskProcessedEvent {
    private final UUID taskId;
    private final String title;
    private final ProcessingStatus status;
    private final String reason;
}
