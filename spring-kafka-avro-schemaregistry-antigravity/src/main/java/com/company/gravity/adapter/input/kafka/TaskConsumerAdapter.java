package com.company.gravity.adapter.input.kafka;

import com.company.gravity.application.usecase.CreateTaskUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskConsumerAdapter {

    private final CreateTaskUseCase createTaskUseCase;

    @KafkaListener(topics = "task-management.task-created.v1", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, GenericRecord> record) {
        log.info("Recebido evento do Kafka. Offset: {}", record.offset());

        GenericRecord event = record.value();
        String correlationId = event.get("correlationId").toString();
        
        // Acessando o payload interno do evento (conforme definido no .avsc)
        GenericRecord payload = (GenericRecord) event.get("payload");
        String title = payload.get("title").toString();
        String description = payload.get("description").toString();

        log.info("Processando criação de task via Kafka. CorrelationId: {}", correlationId);
        
        createTaskUseCase.create(title, description, correlationId);
        
        log.info("Evento processado e task salva no banco local.");
    }
}
