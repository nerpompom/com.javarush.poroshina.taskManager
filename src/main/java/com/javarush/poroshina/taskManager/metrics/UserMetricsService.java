package com.javarush.poroshina.taskManager.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserMetricsService {

    private final Counter successfulLoginsCounter;

    public UserMetricsService(
            MeterRegistry meterRegistry
    ) {
        this.successfulLoginsCounter = Counter.builder(
                        "users.login.success"
                )
                .description(
                        "Количество успешных входов пользователей"
                )
                .register(meterRegistry);
    }

    @EventListener
    public void handleUserLogin(
            UserLoginEvent event
    ) {
        successfulLoginsCounter.increment();
    }
}
