package com.javarush.poroshina.taskManager.metrics;

import com.javarush.poroshina.taskManager.config.AppConstants;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserMetricsService {
    private final Counter successfulLoginsCounter;

    public UserMetricsService(MeterRegistry meterRegistry) {
        this.successfulLoginsCounter = Counter.builder(AppConstants.METRIC_USERS_LOGIN_SUCCESS)
                .description(AppConstants.NUMBER_SUCCESSFUL_USER_LOGINS)
                .register(meterRegistry);
    }

    @EventListener
    public void handleUserLogin(UserLoginEvent event) {
        successfulLoginsCounter.increment();
    }
}
