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

### Learn More About the Core Module

For detailed usage instructions and examples, check out the [Core Module Documentation](core/README.md).

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
`