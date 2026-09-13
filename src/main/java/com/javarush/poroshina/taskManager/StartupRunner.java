package com.javarush.poroshina.taskManager;

import com.javarush.poroshina.taskManager.service.TestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final TestService testService;

    public StartupRunner(TestService testService) {
        this.testService = testService;
    }

    @Override
    public void run(String... args) {
        testService.testDatabase();
    }
}