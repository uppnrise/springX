package dev.bnacar.springx.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiVersionRequestMappingHandlerMappingTest {

    private final ApiVersionRequestMappingHandlerMapping handlerMapping = new ApiVersionRequestMappingHandlerMapping();

    @Test
    public void testGetMappingForMethodWithApiVersion() throws NoSuchMethodException {
        // Arrange
        Method method = TestController.class.getMethod("testMethod");

        // Act
        RequestMappingInfo mappingInfo = handlerMapping.getMappingForMethod(method, TestController.class);

        // Assert
        assertNotNull(mappingInfo);

        PatternsRequestCondition patternsCondition = mappingInfo.getPatternsCondition();
        assertNotNull(patternsCondition);
        assertTrue(patternsCondition.getPatterns().contains("/v1/test"));
    }

    @Test
    public void testGetMappingForMethodWithoutApiVersion() throws NoSuchMethodException {
        // Arrange
        Method method = TestControllerNoVersion.class.getMethod("testMethod");

        // Act
        RequestMappingInfo mappingInfo = handlerMapping.getMappingForMethod(method, TestControllerNoVersion.class);

        // Assert
        assertNotNull(mappingInfo);

        PatternsRequestCondition patternsCondition = mappingInfo.getPatternsCondition();
        assertNotNull(patternsCondition);
        assertTrue(patternsCondition.getPatterns().contains("/test"));
    }

    @ApiVersion("1")
    @RestController
    @RequestMapping("/test")
    static class TestController {
        @GetMapping
        public String testMethod() {
            return "test";
        }
    }

    @RestController
    @RequestMapping("/test")
    static class TestControllerNoVersion {
        @GetMapping
        public String testMethod() {
            return "test";
        }
    }
}
