package dev.bnacar.springx.observability.aspect;

import dev.bnacar.springx.observability.annotation.Metric;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@ConditionalOnProperty(name = "springx.observability.metrics.enabled", havingValue = "true", matchIfMissing = true)
public class MetricAspect {

    private final MeterRegistry meterRegistry;

    public MetricAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("@annotation(dev.bnacar.springx.observability.annotation.Metric)")
    public Object metric(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Metric annotation = method.getAnnotation(Metric.class);

        String metricName = getMetricName(annotation, method);
        List<Tag> tags = getTags(annotation);

        if (annotation.recordInvocationCount()) {
            Counter.builder(metricName + ".count")
                    .description("Number of invocations of " + method.getName())
                    .tags(tags)
                    .register(meterRegistry)
                    .increment();
        }

        if (annotation.recordExecutionTime()) {
            Timer timer = Timer.builder(metricName + ".time")
                    .description("Execution time of " + method.getName())
                    .tags(tags)
                    .register(meterRegistry);

            Timer.Sample sample = Timer.start(meterRegistry);
            try {
                Object result = joinPoint.proceed();
                sample.stop(timer);
                return result;
            } catch (Throwable e) {
                sample.stop(timer);
                recordException(annotation, tags, metricName, method, e);
                throw e;
            }
        } else {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                recordException(annotation, tags, metricName, method, e);
                throw e;
            }
        }
    }

    private void recordException(Metric annotation, List<Tag> tags, String metricName, Method method, Throwable e) {
        if (annotation.recordExceptions()) {
            List<Tag> exceptionTags = new ArrayList<>(tags);
            exceptionTags.add(Tag.of("exception", e.getClass().getSimpleName()));

            Counter.builder(metricName + ".exceptions")
                    .description("Number of exceptions thrown by " + method.getName())
                    .tags(exceptionTags)
                    .register(meterRegistry)
                    .increment();
        }
    }

    private String getMetricName(Metric annotation, Method method) {
        return annotation.value().isEmpty() ? method.getDeclaringClass().getSimpleName() + "." + method.getName() : annotation.value();
    }

    private List<Tag> getTags(Metric annotation) {
        List<Tag> tags = new ArrayList<>();
        for (String tagString : annotation.tags()) {
            String[] parts = tagString.split("=", 2);
            if (parts.length == 2) {
                tags.add(Tag.of(parts[0], parts[1]));
            }
        }
        return tags;
    }
}