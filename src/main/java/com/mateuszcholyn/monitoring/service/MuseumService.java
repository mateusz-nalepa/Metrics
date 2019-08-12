package com.mateuszcholyn.monitoring.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MuseumService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MuseumService.class);

    private final Counter peopleCounter;
    private final Timer timer;
    private final List<String> exhibits = new ArrayList<>(Arrays.asList("One", "Two"));
    private final Random random = new Random();

    public MuseumService(MeterRegistry meterRegistry) {
        Gauge.builder("exhibits.size", exhibits, List::size).tags("name", "museum").register(meterRegistry);
        this.peopleCounter = Counter.builder("people.counter").tags("name", "museum").register(meterRegistry);
        this.timer = Timer.builder("visit.time").tags("name", "museum").register(meterRegistry);
    }

    public void visitMuseum() {
        timer.record(() -> {
            LOGGER.info("Entering museum...");
            peopleCounter.increment();
            try {
                Thread.sleep(random.nextInt(200 - 50) + 50);
            } catch (InterruptedException ex) {
                LOGGER.error("Enter museum has thrown an error", ex);
            }
            LOGGER.info("Leaving museum...");
        });
    }

    @Scheduled(fixedRate = 5000)
    public void changeExhibitSize() {
        int exhibitSize = random.nextInt(10);
        exhibits.clear();
        List<String> newExhibits = IntStream.iterate(0, operand -> operand++)
                .limit(exhibitSize)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        exhibits.addAll(newExhibits);
    }

}
