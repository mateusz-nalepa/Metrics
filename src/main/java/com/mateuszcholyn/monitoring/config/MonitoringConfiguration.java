package com.mateuszcholyn.monitoring.config;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.vavr.API;
import io.vavr.collection.List;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class MonitoringConfiguration {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
        return registry -> registry.config().commonTags("host", getHostName());
    }

    private String getHostName() {
        return API.Try(InetAddress::getLocalHost)
                .map(InetAddress::getHostName)
                .getOrElse("localhost");
    }

    @Bean
    public MeterFilter meterFilter() {
        return new MeterFilter() {
            @Override
            public MeterFilterReply accept(Meter.Id id) {
                return metricsShouldBeBlocked(id)
                        ? MeterFilterReply.DENY
                        : MeterFilterReply.NEUTRAL;
            }
        };
    }

    private boolean metricsShouldBeBlocked(Meter.Id id) {
        return !BLOCKED_METRICS
                .filter(s -> id.getName().startsWith(s))
                .isEmpty();
    }

    private static final List<String> BLOCKED_METRICS = API.List(
            "tomcat",
            "logback",
            "jvm",
            "process",
            "system",
            "http"
    );
}
