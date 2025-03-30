package dev.bnacar.springx.testing.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bnacar.springx.testing.annotation.JsonTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Parameter resolver for JSON tests.
 * Resolves parameters for test methods based on the test scenario.
 */
public class JsonTestParameterResolver implements ParameterResolver {

    private final String testType;
    private final Class<?> testClass;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JsonTestParameterResolver with the specified test type and test class.
     *
     * @param testType the type of test being run
     * @param testClass the class being tested
     */
    public JsonTestParameterResolver(String testType, Class<?> testClass) {
        this.testType = testType;
        this.testClass = testClass;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return parameterType.equals(Object.class) ||
                parameterType.equals(testClass) ||
                parameterType.equals(JacksonTester.class) ||
                parameterType.equals(ObjectMapper.class) ||
                parameterType.equals(getTestDtoClass(extensionContext));
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> parameterType = parameterContext.getParameter().getType();

        if (parameterType.equals(ObjectMapper.class)) {
            return objectMapper;
        }

        if (parameterType.equals(JacksonTester.class)) {
            return new JacksonTester<>(testClass, ResolvableType.forClass(testClass), objectMapper);
        }

        if (parameterType.equals(getTestDtoClass(extensionContext))) {
            return createBasicTestObject();
        }

        // Create test object based on test type
        if (testType.equals("basicTest")) {
            return createBasicTestObject();
        } else if (testType.equals("nullValuesTest")) {
            return createNullValuesTestObject();
        } else if (testType.equals("emptyCollectionsTest")) {
            return createEmptyCollectionsTestObject();
        } else if (testType.equals("caseSensitivityTest")) {
            return createCaseSensitivityTestObject();
        }

        // Default case
        return createBasicTestObject();
    }

    private Class<?> getTestDtoClass(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestMethod().getAnnotation(JsonTest.class).value();
    }

    private Object createBasicTestObject() {
        try {
            Constructor<?> constructor = testClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();

            // Set some basic values on the fields
            for (Field field : testClass.getDeclaredFields()) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();

                if (fieldType.equals(String.class)) {
                    field.set(instance, "test-" + field.getName());
                } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                    field.set(instance, 42);
                } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                    field.set(instance, 42L);
                } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                    field.set(instance, true);
                } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                    field.set(instance, 42.0);
                } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                    field.set(instance, 42.0f);
                } else if (fieldType.equals(java.util.List.class)) {
                    field.set(instance, new ArrayList<>());
                } else if (fieldType.equals(java.util.Map.class)) {
                    field.set(instance, new HashMap<>());
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test object", e);
        }
    }

    private Object createNullValuesTestObject() {
        try {
            Constructor<?> constructor = testClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();

            // Set null values on the fields
            for (Field field : testClass.getDeclaredFields()) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();

                if (!fieldType.isPrimitive()) {
                    field.set(instance, null);
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test object", e);
        }
    }

    private Object createEmptyCollectionsTestObject() {
        try {
            Constructor<?> constructor = testClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();

            // Set empty collections on the fields
            for (Field field : testClass.getDeclaredFields()) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();

                if (fieldType.equals(java.util.List.class)) {
                    field.set(instance, Collections.emptyList());
                } else if (fieldType.equals(java.util.Set.class)) {
                    field.set(instance, Collections.emptySet());
                } else if (fieldType.equals(java.util.Map.class)) {
                    field.set(instance, Collections.emptyMap());
                } else if (fieldType.equals(String.class)) {
                    field.set(instance, "");
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test object", e);
        }
    }

    private Object createCaseSensitivityTestObject() {
        // For case sensitivity testing, we just use the basic test object
        return createBasicTestObject();
    }
}