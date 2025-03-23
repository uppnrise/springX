package dev.bnacar.springx.core.config;

import dev.bnacar.springx.core.aop.LogExecutionTimeAspect;
import dev.bnacar.springx.core.aop.RetryAspect;
import dev.bnacar.springx.core.aop.ThrottleAspect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringXAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SpringXAutoConfiguration.class));

    @Test
    public void testDefaultAutoConfiguration() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(LogExecutionTimeAspect.class);
            assertThat(context).hasSingleBean(RetryAspect.class);
            assertThat(context).hasSingleBean(ThrottleAspect.class);
            assertThat(context).hasSingleBean(SpringXProperties.class);
        });
    }

    @Test
    public void testDisabledLogExecutionTimeAspect() {
        contextRunner
                .withPropertyValues("springx.aop.log-execution-time.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(LogExecutionTimeAspect.class);
                    assertThat(context).hasSingleBean(RetryAspect.class);
                    assertThat(context).hasSingleBean(ThrottleAspect.class);
                });
    }

    @Test
    public void testDisabledRetryAspect() {
        contextRunner
                .withPropertyValues("springx.aop.retry.enabled=false")
                .run(context -> {
                    assertThat(context).hasSingleBean(LogExecutionTimeAspect.class);
                    assertThat(context).doesNotHaveBean(RetryAspect.class);
                    assertThat(context).hasSingleBean(ThrottleAspect.class);
                });
    }

    @Test
    public void testDisabledThrottleAspect() {
        contextRunner
                .withPropertyValues("springx.aop.throttle.enabled=false")
                .run(context -> {
                    assertThat(context).hasSingleBean(LogExecutionTimeAspect.class);
                    assertThat(context).hasSingleBean(RetryAspect.class);
                    assertThat(context).doesNotHaveBean(ThrottleAspect.class);
                });
    }

    @Test
    public void testAllAspectsDisabled() {
        contextRunner
                .withPropertyValues(
                        "springx.aop.log-execution-time.enabled=false",
                        "springx.aop.retry.enabled=false",
                        "springx.aop.throttle.enabled=false"
                )
                .run(context -> {
                    assertThat(context).doesNotHaveBean(LogExecutionTimeAspect.class);
                    assertThat(context).doesNotHaveBean(RetryAspect.class);
                    assertThat(context).doesNotHaveBean(ThrottleAspect.class);
                });
    }
}
