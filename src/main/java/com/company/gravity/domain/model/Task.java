package com.company.gravity.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class Task {
    private UUID id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    public static Task create(String title, String description) {
        return Task.builder()
                .id(UUID.randomUUID())
                .title(title)
                .description(description)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
