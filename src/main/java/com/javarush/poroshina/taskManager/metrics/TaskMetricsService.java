package com.javarush.poroshina.taskManager.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class TaskMetricsService {

    private final Counter tasksCreatedCounter;

    public TaskMetricsService(
            MeterRegistry meterRegistry
    ) {
        this.tasksCreatedCounter = Counter.builder(
                        "tasks.created"
                )
                .description(
                        "Количество созданных задач"
                )
                .register(meterRegistry);
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleTaskCreated(
            TaskCreatedEvent event
    ) {
        tasksCreatedCounter.increment();
    }
}
