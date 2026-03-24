package com.company.gravity.domain.event;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class TaskCreatedEvent {
    private UUID taskId;
    private String title;
    private String description;
    private String status;
}
