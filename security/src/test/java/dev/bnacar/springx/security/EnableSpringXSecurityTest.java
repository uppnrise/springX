package dev.bnacar.springx.security;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import dev.bnacar.springx.security.config.SecurityAutoConfiguration;

import static org.junit.jupiter.api.Assertions.*;

class EnableSpringXSecurityTest {

    @Test
    void enableSpringXSecurity_shouldHaveCorrectRetention() {
        Retention retention = EnableSpringXSecurity.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void enableSpringXSecurity_shouldHaveCorrectTarget() {
        Target target = EnableSpringXSecurity.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertArrayEquals(new ElementType[]{ElementType.TYPE}, target.value());
    }

    @Test
    void enableSpringXSecurity_shouldImportSecurityAutoConfiguration() {
        Import importAnnotation = EnableSpringXSecurity.class.getAnnotation(Import.class);
        assertNotNull(importAnnotation);
        assertArrayEquals(new Class[]{SecurityAutoConfiguration.class}, importAnnotation.value());
    }

    // Example class with @EnableSpringXSecurity annotation for testing
    @EnableSpringXSecurity
    private static class TestConfiguration {
    }

    @Test
    void enableSpringXSecurity_shouldBeApplicableToClasses() {
        EnableSpringXSecurity annotation = TestConfiguration.class.getAnnotation(EnableSpringXSecurity.class);
        assertNotNull(annotation);
    }
}
