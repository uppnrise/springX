package dev.bnacar.springx.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Aspect that handles the {@link LogExecutionTime} annotation.
 * Logs the execution time of methods annotated with {@link LogExecutionTime}.
 */
@Aspect
@Component
@ConditionalOnProperty(name = "spring-x.aop.log-execution-time.enabled", havingValue = "true", matchIfMissing = true)
public class LogExecutionTimeAspect {

    /**
     * Intercepts method calls annotated with {@link LogExecutionTime} and logs their execution time.
     *
     * @param joinPoint the join point representing the intercepted method call
     * @return the result of the method call
     * @throws Throwable if the method call throws an exception
     */
    @Around("@annotation(dev.bnacar.springx.core.aop.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LogExecutionTime annotation = method.getAnnotation(LogExecutionTime.class);

        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Executing ").append(className).append(".").append(methodName);

        if (annotation.includeArgs()) {
            logMessage.append(" with arguments: ").append(Arrays.toString(joinPoint.getArgs()));
        }

        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;

            logMessage.append(" completed in ").append(executionTime).append("ms");

            switch (annotation.value()) {
                case TRACE:
                    logger.trace(logMessage.toString());
                    break;
                case DEBUG:
                    logger.debug(logMessage.toString());
                    break;
                case INFO:
                    logger.info(logMessage.toString());
                    break;
                case WARN:
                    logger.warn(logMessage.toString());
                    break;
                case ERROR:
                    logger.error(logMessage.toString());
                    break;
            }
        }
    }
}
