package dev.bnacar.springx.web.controller;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo.BuilderConfiguration;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Custom request mapping handler that applies API versioning to controllers
 * annotated with {@link ApiVersion}.
 */
public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final BuilderConfiguration config = new BuilderConfiguration();

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);

        if (info == null) {
            return null;
        }

        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        if (apiVersion == null) {
            return info;
        }

        String version = apiVersion.value();
        String prefix = "/v" + version;

        return RequestMappingInfo
                .paths(prefix)
                .options(config)
                .build()
                .combine(info);
    }
}
