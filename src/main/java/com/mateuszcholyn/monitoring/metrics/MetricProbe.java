package com.mateuszcholyn.monitoring.metrics;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.vavr.collection.Set;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToDoubleFunction;

abstract class MetricProbe implements MetricFunctions {

    private final ConcurrentHashMap<NameWithTags, Timer> timers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<NameWithTags, DistributionSummary> summaries = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<NameWithTags, Gauge> gauges = new ConcurrentHashMap<>();

    private final MeterRegistry meterRegistry;

    protected MetricProbe(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    protected <T> void registerGauge(Set<Tag> tags, String methodName, T t, ToDoubleFunction<T> toDoubleFunction) {
        NameWithTags nameWithTags = new NameWithTags(methodName, tags);
        if (!timers.containsKey(nameWithTags)) {
            Gauge gauge = Gauge.builder(methodName, t, toDoubleFunction).register(meterRegistry);
            gauges.put(nameWithTags, gauge);
        }
    }

    protected Timer getOrCreateTimer(Set<Tag> tags, String methodName) {
        NameWithTags nameWithTags = new NameWithTags(methodName, tags);
        if (timers.containsKey(nameWithTags)) {
            return timers.get(nameWithTags);
        } else {
            Timer timer = Timer.builder(methodName).tags(tags).register(meterRegistry);
            timers.put(nameWithTags, timer);
            return timer;
        }
    }

    protected DistributionSummary getOrCreateSummary(Set<Tag> tags, String methodName) {
        NameWithTags nameWithTags = new NameWithTags(methodName, tags);
        if (summaries.containsKey(nameWithTags)) {
            return summaries.get(nameWithTags);
        } else {
            DistributionSummary distSummary = DistributionSummary.builder(methodName).tags(tags).register(meterRegistry);
            summaries.put(nameWithTags, distSummary);
            return distSummary;
        }
    }

    static class NameWithTags {

        private final String name;
        private final Set<Tag> tags;

        NameWithTags(String name, Set<Tag> tags) {
            this.name = name;
            this.tags = tags;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NameWithTags that = (NameWithTags) o;
            return name.equals(that.name) &&
                    tags.equals(that.tags);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, tags);
        }
    }

}


