package com.mateuszcholyn.monitoring.metrics;

import io.micrometer.core.instrument.Timer;
import io.vavr.Function1;

public interface MetricFunctions {

    @FunctionalInterface
    interface MetricFunction3<T1 extends String, T2, T3, R extends String> {
        R callWithMetrics(T1 t1, T2 t2, T3 t3);
    }

    @FunctionalInterface
    interface MetricFunction2<T1 extends String, T2, R extends String> {
        R callWithMetrics(T1 t1, T2 t2);
    }

    @FunctionalInterface
    interface MetricFunction1<T1, R> {
        R callWithMetrics(T1 t1);
    }

//    default <T2, T3> MetricFunction3<String, T2, T3, String> createProxyFunction3(Function3<String, T2, T3, String> function2, String methodName) {
//        return (s, t2, t3) -> wrap(s, t3, function2.apply(s, t2), methodName);
//    }
//
//    default <T2> MetricFunction2<String, T2, String> createProxyFunction2(Function2<String, T2, String> function2, String methodName) {
//        return (s, t2) -> wrap(s, t2, function2.apply(s), methodName);
//    }

    default <T, R> MetricFunction1<T, R> createProxyFunction1(Function1<T, R> function1, String methodName) {
        return rq -> wrap(rq, rq, function1, methodName);
    }

    <T, LAST, R> R wrap(T rq, LAST lastParameter, Function1<LAST, R> proxyFunction1, String methodName);

}
