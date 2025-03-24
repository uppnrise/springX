package dev.bnacar.springx.web.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to apply versioning to REST controllers.
 * When applied to a controller class, it will prefix all endpoints with the specified version.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {

    /**
     * The API version to use for the controller.
     * @return the API version
     */
    String value();
}
