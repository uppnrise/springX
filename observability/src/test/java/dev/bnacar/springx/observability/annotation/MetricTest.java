package dev.bnacar.springx.observability.annotation;

import org.junit.jupiter.api.Test;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class MetricTest {

    @Test
    void metric_shouldHaveCorrectRetention() {
        Retention retention = Metric.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void metric_shouldHaveCorrectTarget() {
        Target target = Metric.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertArrayEquals(new ElementType[]{ElementType.METHOD}, target.value());
    }

    @Test
    void metric_defaultValueShouldBeEmptyString() throws NoSuchMethodException {
        Method valueMethod = Metric.class.getDeclaredMethod("value");
        String defaultValue = (String) valueMethod.getDefaultValue();
        assertNotNull(defaultValue);
        assertEquals("", defaultValue);
    }

    @Test
    void metric_defaultDescriptionShouldBeEmptyString() throws NoSuchMethodException {
        Method descriptionMethod = Metric.class.getDeclaredMethod("description");
        String defaultValue = (String) descriptionMethod.getDefaultValue();
        assertNotNull(defaultValue);
        assertEquals("", defaultValue);
    }

    @Test
    void metric_defaultTagsShouldBeEmptyArray() throws NoSuchMethodException {
        Method tagsMethod = Metric.class.getDeclaredMethod("tags");
        String[] defaultValue = (String[]) tagsMethod.getDefaultValue();
        assertNotNull(defaultValue);
        assertEquals(0, defaultValue.length);
    }

    @Test
    void metric_defaultRecordExecutionTimeShouldBeTrue() throws NoSuchMethodException {
        Method recordExecutionTimeMethod = Metric.class.getDeclaredMethod("recordExecutionTime");
        boolean defaultValue = (boolean) recordExecutionTimeMethod.getDefaultValue();
        assertTrue(defaultValue);
    }

    @Test
    void metric_defaultRecordInvocationCountShouldBeTrue() throws NoSuchMethodException {
        Method recordInvocationCountMethod = Metric.class.getDeclaredMethod("recordInvocationCount");
        boolean defaultValue = (boolean) recordInvocationCountMethod.getDefaultValue();
        assertTrue(defaultValue);
    }

    @Test
    void metric_defaultRecordExceptionsShouldBeTrue() throws NoSuchMethodException {
        Method recordExceptionsMethod = Metric.class.getDeclaredMethod("recordExceptions");
        boolean defaultValue = (boolean) recordExceptionsMethod.getDefaultValue();
        assertTrue(defaultValue);
    }

    // Example class with @Metric annotation for testing
    private static class TestService {
        @Metric
        public void defaultMetricMethod() {
        }

        @Metric(value = "custom-metric", description = "Custom metric description",
                tags = {"tag1", "tag2"}, recordExecutionTime = false,
                recordInvocationCount = false, recordExceptions = false)
        public void customMetricMethod() {
        }
    }

    @Test
    void metric_shouldBeApplicableToMethods() throws NoSuchMethodException {
        Method defaultMethod = TestService.class.getDeclaredMethod("defaultMetricMethod");
        Metric defaultMetric = defaultMethod.getAnnotation(Metric.class);

        assertNotNull(defaultMetric);
        assertEquals("", defaultMetric.value());
        assertEquals("", defaultMetric.description());
        assertEquals(0, defaultMetric.tags().length);
        assertTrue(defaultMetric.recordExecutionTime());
        assertTrue(defaultMetric.recordInvocationCount());
        assertTrue(defaultMetric.recordExceptions());
    }

    @Test
    void metric_shouldSupportCustomization() throws NoSuchMethodException {
        Method customMethod = TestService.class.getDeclaredMethod("customMetricMethod");
        Metric customMetric = customMethod.getAnnotation(Metric.class);

        assertNotNull(customMetric);
        assertEquals("custom-metric", customMetric.value());
        assertEquals("Custom metric description", customMetric.description());
        assertArrayEquals(new String[]{"tag1", "tag2"}, customMetric.tags());
        assertFalse(customMetric.recordExecutionTime());
        assertFalse(customMetric.recordInvocationCount());
        assertFalse(customMetric.recordExceptions());
    }
}
