package dev.bnacar.springx.security.annotation;

import org.junit.jupiter.api.Test;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class SecuredTest {

    @Test
    void secured_shouldHaveCorrectRetention() {
        Retention retention = Secured.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void secured_shouldHaveCorrectTarget() {
        Target target = Secured.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertArrayEquals(new ElementType[]{ElementType.METHOD, ElementType.TYPE}, target.value());
    }

    @Test
    void secured_defaultValueShouldBeEmptyArray() throws NoSuchMethodException {
        Method valueMethod = Secured.class.getDeclaredMethod("value");
        String[] defaultValue = (String[]) valueMethod.getDefaultValue();
        assertNotNull(defaultValue);
        assertEquals(0, defaultValue.length);
    }

    @Test
    void secured_defaultAllRolesRequiredShouldBeFalse() throws NoSuchMethodException {
        Method allRolesRequiredMethod = Secured.class.getDeclaredMethod("allRolesRequired");
        boolean defaultValue = (boolean) allRolesRequiredMethod.getDefaultValue();
        assertFalse(defaultValue);
    }

    // Example class with @Secured annotation for testing
    private static class TestController {
        @Secured({"ROLE_ADMIN", "ROLE_USER"})
        public void securedMethod() {
        }

        @Secured(value = {"ROLE_ADMIN", "ROLE_MANAGER"}, allRolesRequired = true)
        public void allRolesRequiredMethod() {
        }
    }

    @Test
    void secured_shouldBeApplicableToMethods() throws NoSuchMethodException {
        Method securedMethod = TestController.class.getDeclaredMethod("securedMethod");
        Secured secured = securedMethod.getAnnotation(Secured.class);

        assertNotNull(secured);
        assertArrayEquals(new String[]{"ROLE_ADMIN", "ROLE_USER"}, secured.value());
        assertFalse(secured.allRolesRequired());
    }

    @Test
    void secured_shouldSupportAllRolesRequired() throws NoSuchMethodException {
        Method allRolesRequiredMethod = TestController.class.getDeclaredMethod("allRolesRequiredMethod");
        Secured secured = allRolesRequiredMethod.getAnnotation(Secured.class);

        assertNotNull(secured);
        assertArrayEquals(new String[]{"ROLE_ADMIN", "ROLE_MANAGER"}, secured.value());
        assertTrue(secured.allRolesRequired());
    }
}
