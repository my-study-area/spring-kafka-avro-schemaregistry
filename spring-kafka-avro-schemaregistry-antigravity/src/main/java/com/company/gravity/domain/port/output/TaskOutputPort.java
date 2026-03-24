package com.company.gravity.domain.port.output;

import com.company.gravity.domain.event.EventEnvelope;
import com.company.gravity.domain.event.TaskCreatedEvent;

public interface TaskOutputPort {
    void publishTaskCreated(EventEnvelope<TaskCreatedEvent> event);
}
