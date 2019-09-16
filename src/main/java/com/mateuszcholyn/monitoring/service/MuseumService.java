package com.mateuszcholyn.monitoring.service;

import com.mateuszcholyn.monitoring.metrics.MetricService;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MuseumService extends MetricService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MuseumService.class);

    private final Random random = new Random();
    private final AtomicInteger actualGeneratedNumber = new AtomicInteger(0);

    private final MetricFunction1<Integer, Integer> metricFunction1;

    public MuseumService(MeterRegistry meterRegistry) {
        super(meterRegistry, "Museum");
        registerGauge("number.actual.value", actualGeneratedNumber, AtomicInteger::get);
        this.metricFunction1 = createProxyFunction1(this::internalRandomNumber, "random.number");
    }

    public Integer randomNumber(int bound) {
        return metricFunction1.callWithMetrics(bound);
    }

    private Integer internalRandomNumber(int bound) {
        int randomNumber = random.nextInt(bound);
        actualGeneratedNumber.set(randomNumber);
        uncheckedSleep(randomNumber);
        return randomNumber;
    }

    private void uncheckedSleep(int randomNumber) {
        try {
            Thread.sleep(randomNumber);
        } catch (InterruptedException e) {
            throw new RuntimeException("Sleeping Interrupted", e);
        }
    }

}
