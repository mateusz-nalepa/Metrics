package com.mateuszcholyn.monitoring.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.vavr.API;
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

}
