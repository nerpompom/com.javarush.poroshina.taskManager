package com.javarush.poroshina.taskManager.metrics;

import com.javarush.poroshina.taskManager.config.AppConstants;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class TaskMetricsService {
    private final Counter tasksCreatedCounter;

    public TaskMetricsService(MeterRegistry meterRegistry) {
        this.tasksCreatedCounter = Counter.builder(AppConstants.METRIC_TASKS_CREATED)
                .description(AppConstants.NUMBER_TASKS_CREATED)
                .register(meterRegistry);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTaskCreated(TaskCreatedEvent event) {
        tasksCreatedCounter.increment();
    }
}
