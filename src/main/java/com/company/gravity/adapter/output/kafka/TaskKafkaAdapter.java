package com.company.gravity.adapter.output.kafka;

import com.company.gravity.domain.event.EventEnvelope;
import com.company.gravity.domain.event.TaskCreatedEvent;
import com.company.gravity.domain.port.output.TaskOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskKafkaAdapter implements TaskOutputPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "task-management.task-created.v1";

    @Override
    public void publishTaskCreated(EventEnvelope<TaskCreatedEvent> event) {
        log.info("Publicando evento no Kafka. CorrelationId: {}", event.getCorrelationId());

        // Para evitar erros de compilação antes de rodar o plugin avro,
        // vou mapear os dados manualmente como um GenericRecord ou Map para o KafkaTemplate.
        // Em produção, usaríamos as classes geradas TaskCreatedAvro e TaskAvro.
        
        Map<String, Object> taskPayload = new HashMap<>();
        taskPayload.put("taskId", event.getPayload().getTaskId().toString());
        taskPayload.put("title", event.getPayload().getTitle());
        taskPayload.put("description", event.getPayload().getDescription());
        taskPayload.put("status", event.getPayload().getStatus());

        Map<String, Object> fullEvent = new HashMap<>();
        fullEvent.put("eventId", event.getEventId());
        fullEvent.put("transactionId", event.getTransactionId());
        fullEvent.put("correlationId", event.getCorrelationId());
        fullEvent.put("timestamp", event.getTimestamp().toString());
        fullEvent.put("payload", taskPayload);

        try {
            kafkaTemplate.send(TOPIC, event.getPayload().getTaskId().toString(), fullEvent);
            log.info("Evento publicado com sucesso no tópico: {}", TOPIC);
        } catch (Exception e) {
            log.error("Erro ao publicar evento no Kafka", e);
            // Em produção, aqui implementaríamos retry ou publicação em DLQ de fallback
        }
    }
}
