package com.mateuszcholyn.monitoring.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.vavr.API;
import io.vavr.Function1;
import io.vavr.collection.Set;

import java.util.function.ToDoubleFunction;


public class MetricService extends MetricProbe {

    private static final Double DROPPED_VALUE = -1.0;
    private static final String TAG_SERVICE_NAME = "service.name";
    private static final String TAG_SIZE_TYPE = "size.type";
    private static final String TAG_REQUEST_SIZE = "request.size";
    private static final String TAG_RESPONSE_SIZE = "response.size";

    private final Set<Tag> serviceNameTags;

    protected MetricService(MeterRegistry meterRegistry, String serviceName) {
        super(meterRegistry);
        this.serviceNameTags = API.Set(Tag.of(TAG_SERVICE_NAME, serviceName));
    }

    @Override
    public <T, LAST, R> R wrap(T rq, LAST lastParameter, Function1<LAST, R> proxyFunction1, String methodName) {
        recordRqSize(methodName, rq);
        R response = getOrCreateTimer(serviceNameTags, methodName)
                .record(() -> proxyFunction1.apply(lastParameter));
        recorsRsSize(methodName, response);

        return response;
    }

    private void recordRqSize(String methodName, Object rq) {
        getOrCreateSummary(serviceNameTags.add(Tag.of(TAG_SIZE_TYPE, TAG_REQUEST_SIZE)), methodName)
                .record(calculateSizeFor(rq));
    }

    private void recorsRsSize(String methodName, Object rs) {
        getOrCreateSummary(serviceNameTags.add(Tag.of(TAG_SIZE_TYPE, TAG_RESPONSE_SIZE)), methodName)
                .record(calculateSizeFor(rs));

    }

    private double calculateSizeFor(Object object) {
        if (object instanceof String) {
            return ((String) object).length();
        } else {
            return DROPPED_VALUE;
        }
    }

    protected <T> void registerGauge(String methodName, T t, ToDoubleFunction<T> toDoubleFunction) {
        registerGauge(serviceNameTags, methodName, t, toDoubleFunction);
    }

}
