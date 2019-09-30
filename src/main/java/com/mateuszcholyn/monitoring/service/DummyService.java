package com.mateuszcholyn.monitoring.service;

import com.mateuszcholyn.monitoring.metrics.MetricService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DummyService extends MetricService {

    private final Random random = new Random();
    private final AtomicInteger actualGeneratedNumber = new AtomicInteger(0);

    private final MetricFunction1<String, String> metricFunction1;

    public DummyService(MeterRegistry meterRegistry) {
        super(meterRegistry, "Museum");
        registerGauge("number.actual.value", actualGeneratedNumber, AtomicInteger::get);
        this.metricFunction1 = createProxyFunction1(this::internalRandomNumber, "random.number");
    }

    public String randomNumber(String bound) {
        return metricFunction1.callWithMetrics(bound);
    }

    private String internalRandomNumber(String bound) {
        int randomNumber = random.nextInt(Integer.parseInt(bound));
        actualGeneratedNumber.set(randomNumber);
        uncheckedSleep(randomNumber);
        return bound;
    }

    private void uncheckedSleep(int randomNumber) {
        try {
            Thread.sleep(randomNumber);
        } catch (InterruptedException e) {
            throw new RuntimeException("Sleeping Interrupted", e);
        }
    }

}
