package com.company.gravity.application.usecase;

import com.company.gravity.domain.model.Task;

public interface CreateTaskUseCase {
    Task create(String title, String description, String correlationId);
}
