package com.mateuszcholyn.monitoring.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.vavr.Function1;

import java.util.function.ToDoubleFunction;

public class MetricService implements MetricFunctions {

    private final MeterRegistry meterRegistry;
    private final String serviceName;

    protected MetricService(MeterRegistry meterRegistry, String serviceName) {
        this.meterRegistry = meterRegistry;
        this.serviceName = serviceName;
    }

    @Override
    public <T, LAST, R> R wrap(T rq, LAST lastParameter, Function1<LAST, R> proxyFunction1, String methodName) {
        Timer timer = Timer.builder(methodName).register(meterRegistry);
        return timer.record(() -> proxyFunction1.apply(lastParameter));
    }

    protected <T> void registerGauge(String name, T t, ToDoubleFunction<T> toDoubleFunction) {
        Gauge.builder(name, t, toDoubleFunction).register(meterRegistry);
    }

}
