package com.company.gravity.adapter.output.kafka;

import com.company.gravity.domain.event.EventEnvelope;
import com.company.gravity.domain.event.TaskCreatedEvent;
import com.company.gravity.domain.event.TaskProcessedEvent;
import com.company.gravity.domain.port.output.TaskOutputPort;
import com.company.gravity.domain.port.output.TaskResponseOutputPort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskKafkaAdapter implements TaskOutputPort, TaskResponseOutputPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String CREATED_TOPIC = "task-management.task-created.v1";
    private static final String PROCESSED_TOPIC = "task-management.task-processed.v1";

    private Schema createdSchema;
    private Schema processedSchema;

    @PostConstruct
    public void init() throws IOException {
        this.createdSchema = new Schema.Parser().parse(getClass().getResourceAsStream("/avro/task_created.avsc"));
        this.processedSchema = new Schema.Parser().parse(getClass().getResourceAsStream("/avro/task_processed.avsc"));
    }

    @Override
    public void publishTaskCreated(EventEnvelope<TaskCreatedEvent> event) {
        log.info("Publicando evento no Kafka. CorrelationId: {}", event.getCorrelationId());

        Schema payloadSchema = createdSchema.getField("payload").schema();
        GenericRecord taskPayload = new GenericData.Record(payloadSchema);
        taskPayload.put("taskId", event.getPayload().getTaskId().toString());
        taskPayload.put("title", event.getPayload().getTitle());
        taskPayload.put("description", event.getPayload().getDescription());
        taskPayload.put("status", event.getPayload().getStatus());

        send(CREATED_TOPIC, event.getPayload().getTaskId().toString(), event, createdSchema, taskPayload);
    }

    @Override
    public void sendFeedback(EventEnvelope<TaskProcessedEvent> event) {
        log.info("Publicando feedback no Kafka. Status: {}, CorrelationId: {}",
                event.getPayload().getStatus(), event.getCorrelationId());

        Schema payloadSchema = processedSchema.getField("payload").schema();
        GenericRecord feedbackPayload = new GenericData.Record(payloadSchema);
        feedbackPayload.put("taskId",
                event.getPayload().getTaskId() != null ? event.getPayload().getTaskId().toString() : null);
        feedbackPayload.put("title", event.getPayload().getTitle());
        feedbackPayload.put("status", event.getPayload().getStatus().name());
        feedbackPayload.put("reason", event.getPayload().getReason());

        String key = event.getPayload().getTaskId() != null ? event.getPayload().getTaskId().toString()
                : event.getCorrelationId();

        send(PROCESSED_TOPIC, key, event, processedSchema, feedbackPayload);
    }

    private void send(String topic, String key, EventEnvelope<?> envelope, Schema recordSchema, GenericRecord payload) {
        GenericRecord fullEvent = new GenericData.Record(recordSchema);
        fullEvent.put("eventId", envelope.getEventId());
        fullEvent.put("transactionId", envelope.getTransactionId());
        fullEvent.put("correlationId", envelope.getCorrelationId());
        fullEvent.put("timestamp", envelope.getTimestamp().toString());
        fullEvent.put("payload", payload);

        try {
            kafkaTemplate.send(topic, key, fullEvent);
            log.info("Evento publicado com sucesso no tópico: {}", topic);
        } catch (Exception e) {
            log.error("Erro ao publicar evento no tópico: {}", topic, e);
        }
    }
}
