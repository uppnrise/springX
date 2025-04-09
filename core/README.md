# Spring X Core Module

The Core module of Spring X provides powerful Aspect-Oriented Programming (AOP) utilities to simplify common cross-cutting concerns in Spring Boot applications. These utilities are designed to be lightweight, easy to use, and configurable.

## Features

- **Log Execution Time**: Measure and log the execution time of methods.
- **Retry Mechanism**: Automatically retry failed operations with configurable parameters.
- **Rate Limiting**: Throttle method invocations to control the rate of execution.

## Usage Examples

### Logging Method Execution Time

```java
import dev.bnacar.springx.core.aop.LogExecutionTime;

@Service
public class MyService {
    
    @LogExecutionTime
    public void performOperation() {
        // Method implementation
    }
    
    @LogExecutionTime(value = LogExecutionTime.LogLevel.INFO, includeArgs = true)
    public String processData(String input) {
        // Method implementation
        return "Processed: " + input;
    }
}
```

### Automatic Retry for Failed Operations

```java
import dev.bnacar.springx.core.aop.Retry;

@Service
public class ExternalServiceClient {
    
    @Retry(maxAttempts = 3, backoffMs = 1000)
    public ExternalData fetchData() {
        // Method that might fail due to network issues
        return externalService.getData();
    }
    
    @Retry(
        maxAttempts = 5, 
        exponential = true, 
        retryOn = {IOException.class, TimeoutException.class},
        noRetryOn = {AuthenticationException.class}
    )
    public void sendData(Data data) {
        // Method implementation
    }
}
```

### Rate Limiting

```java
import dev.bnacar.springx.core.aop.Throttle;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Throttle(limit = 10, timeUnit = TimeUnit.MINUTES)
    @GetMapping("/sensitive-data")
    public SensitiveData getSensitiveData() {
        // Method implementation
        return sensitiveDataService.getData();
    }
    
    @Throttle(
        limit = 5, 
        timeUnit = TimeUnit.SECONDS,
        behavior = Throttle.ThrottleBehavior.RETURN_DEFAULT
    )
    @PostMapping("/process")
    public Result processData(@RequestBody Data data) {
        // Method implementation
        return dataProcessor.process(data);
    }
}
```

## Configuration

The Core module can be configured through your application.properties or application.yml file:

```properties
# Enable/disable aspects
spring-x.aop.log-execution-time.enabled=true
spring-x.aop.retry.enabled=true
spring-x.aop.throttle.enabled=true
```

## Requirements

- Java 17 or higher
- Spring Boot 3.x
