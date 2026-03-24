package com.company.gravity.domain.port.output;

import com.company.gravity.domain.event.EventEnvelope;
import com.company.gravity.domain.event.TaskProcessedEvent;

/**
 * Porta de saída para envio de feedback de processamento de tarefas.
 */
public interface TaskResponseOutputPort {
    void sendFeedback(EventEnvelope<TaskProcessedEvent> feedback);
}
