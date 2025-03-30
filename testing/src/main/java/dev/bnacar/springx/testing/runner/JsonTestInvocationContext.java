package dev.bnacar.springx.testing.runner;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import java.util.Collections;
import java.util.List;

/**
 * Invocation context for JSON tests.
 * Provides the test display name and extensions for each test invocation.
 */
public class JsonTestInvocationContext implements TestTemplateInvocationContext {

    private final String displayName;
    private final Class<?> testClass;

    /**
     * Constructs a new JsonTestInvocationContext with the specified display name and test class.
     *
     * @param displayName the display name for the test invocation
     * @param testClass the class being tested
     */
    public JsonTestInvocationContext(String displayName, Class<?> testClass) {
        this.displayName = displayName;
        this.testClass = testClass;
    }

    @Override
    public String getDisplayName(int invocationIndex) {
        return String.format("%s [%s]", displayName, testClass.getSimpleName());
    }

    @Override
    public List<Extension> getAdditionalExtensions() {
        return Collections.singletonList(new JsonTestParameterResolver(displayName, testClass));
    }
}