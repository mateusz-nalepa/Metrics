package com.mateuszcholyn.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RunAtStart implements ApplicationRunner {

    @Autowired
    MeterRegistry meterRegistry;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Started");
    }
}
