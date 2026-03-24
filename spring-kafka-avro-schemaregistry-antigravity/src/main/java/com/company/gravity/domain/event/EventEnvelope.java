package com.company.gravity.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class EventEnvelope<T> {
    private String eventId;
    private String transactionId;
    private String correlationId;
    private LocalDateTime timestamp;
    private T payload;

    public static <T> EventEnvelope<T> wrap(T payload, String correlationId) {
        return EventEnvelope.<T>builder()
                .eventId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .correlationId(correlationId)
                .timestamp(LocalDateTime.now())
                .payload(payload)
                .build();
    }
}
