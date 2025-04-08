# Spring X

![Build Status](https://github.com/uppnrise/springX/actions/workflows/gradle.yml/badge.svg)
<!-- ![Maven Central](https://img.shields.io/maven-central/v/dev.bnacar/springX) -->
<!-- ![License](https://img.shields.io/github/license/uppnrise/springX) -->

Spring X is a utility library designed to enhance Spring Boot applications by providing solutions to common pain points developers face. The library aims to be lightweight, easy to integrate, and provide immediate value with minimal configuration.

## Features

- **Aspect-Oriented Utilities**: Annotations like `@LogExecutionTime`, `@Retry`, and `@Throttle` to simplify common cross-cutting concerns
- **Minimal Configuration**: Works out-of-the-box with sensible defaults
- **Modular Design**: Use only what you need with our modular architecture
- **Production-Ready**: Thoroughly tested and performance-optimized

## Modules

- **Core**: Base functionality and shared utilities
- **Web**: Enhancements for Spring MVC and REST applications
- **Data**: Utilities for data access and manipulation
- **Security**: Security enhancements and simplifications
- **Observability**: Monitoring and observability tools
- **Testing**: Testing utilities to simplify test creation

## Getting Started

### Maven

```xml
<dependency>
    <groupId>dev.bnacar</groupId>
    <artifactId>springx</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'dev.bnacar:springx:0.1.0'
```

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

Spring X can be configured through your application.properties or application.yml file:

```properties
# Enable/disable aspects
spring-x.aop.log-execution-time.enabled=true
spring-x.aop.retry.enabled=true
spring-x.aop.throttle.enabled=true

# Additional configuration options will be available in future releases
```

## Requirements

- Java 17 or higher
- Spring Boot 3.x

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the `LICENSE` file for details.