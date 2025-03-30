package dev.bnacar.springx.testing.runner;

import dev.bnacar.springx.testing.annotation.JsonTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * JUnit 5 extension that provides test template invocation contexts for methods annotated with {@link JsonTest}.
 * This extension runs multiple test scenarios for JSON serialization/deserialization.
 */
public class JsonTestExtension implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getTestMethod()
                .map(method -> method.isAnnotationPresent(JsonTest.class))
                .orElse(false);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        Method testMethod = context.getRequiredTestMethod();
        JsonTest annotation = testMethod.getAnnotation(JsonTest.class);
        Class<?> testClass = annotation.value();

        List<TestTemplateInvocationContext> contexts = new ArrayList<>();

        // Basic serialization/deserialization test
        contexts.add(new JsonTestInvocationContext("basicTest", testClass));

        // Test null values if enabled
        if (annotation.testNullValues()) {
            contexts.add(new JsonTestInvocationContext("nullValuesTest", testClass));
        }

        // Test empty collections if enabled
        if (annotation.testEmptyCollections()) {
            contexts.add(new JsonTestInvocationContext("emptyCollectionsTest", testClass));
        }

        // Test case sensitivity if enabled
        if (annotation.testCaseSensitivity()) {
            contexts.add(new JsonTestInvocationContext("caseSensitivityTest", testClass));
        }

        return contexts.stream();
    }
}
