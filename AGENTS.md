# Pelee Development Guide

This file contains development guidelines and commands for agentic coding agents working on the Pelee project.

## Project Overview

Pelee is a Java-based data processing pipeline framework built with Spring Boot that provides a plugin-based architecture for transforming JSON data through configurable pipelines.

- **Technology Stack**: Java 17+, Spring Boot 2.7.14, Gradle with Kotlin DSL, Jackson for JSON processing, Lombok for boilerplate reduction
- **Architecture**: Domain-Driven Design (DDD) with plugin-based stage processing
- **Build System**: Gradle wrapper with Kotlin DSL

## Build, Lint, and Test Commands

### Core Commands
```bash
# Build the entire project
./gradlew build

# Clean and build
./gradlew clean build

# Run the application (Spring Boot)
./gradlew bootRun

# Assemble JAR files
./gradlew bootJar

# Run tests
./gradlew test

# Run all checks (includes tests)
./gradlew check

# Clean build directory
./gradlew clean
```

### Running Single Tests
```bash
# Run a specific test class
./gradlew test --tests "com.example.YourTestClass"

# Run a specific test method
./gradlew test --tests "com.example.YourTestClass.testMethodName"

# Run tests in a package
./gradlew test --tests "priv.eric.pelee.*"

# Run tests with verbose output
./gradlew test --info
```

### Development Tasks
```bash
# Generate Javadoc
./gradlew javadoc

# View project dependencies
./gradlew dependencies

# Check dependency updates
./gradlew dependencyUpdates
```

## Code Style Guidelines

### Package Structure
Follow Domain-Driven Design (DDD) principles:

```
priv.eric.pelee/
├── application/          # Application layer (orchestration)
│   ├── init/            # Initialization components
│   ├── loader/          # Pipeline loading logic
│   ├── registry/        # Plugin and pipeline registries
│   ├── service/         # Business services
│   └── util/            # Application utilities
├── domain/              # Domain layer (core business logic)
│   └── model/          # Core domain models
├── infrastructure/      # Infrastructure layer
│   ├── repository/     # Data access
│   ├── util/          # Technical utilities
│   └── exception/     # Custom exceptions
└── interfaces/         # Interface layer
    ├── controller/    # REST controllers
    ├── entity/        # DTOs and response entities
    ├── config/        # Configuration classes
    └── convertor/     # Data converters
```

### Import Organization
```java
// Standard library imports first
import java.util.*;
import java.util.concurrent.*;

// Third-party imports (Jackson, Spring, etc.)
import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

// Domain imports
import priv.eric.pelee.domain.model.*;
import priv.eric.pelee.domain.service.*;

// Application imports
import priv.eric.pelee.application.service.*;
import priv.eric.pelee.application.registry.*;

// Infrastructure imports
import priv.eric.pelee.infrastructure.util.*;

// Plugin imports
import priv.eric.plugin.stage.rename.*;
```

### Naming Conventions

#### Classes and Interfaces
- **Classes**: `PascalCase` with descriptive names
  - Controllers: `XxxController` (e.g., `DialogRecordController`)
  - Services: `XxxService` (e.g., `PipelineExecutionService`)
  - Models/Entities: Domain-specific names (e.g., `Pipeline`, `Stage`, `Event`)
  - Plugins: `XxxStage` (e.g., `RenameStage`, `RemoveStage`)
  - Configurations: `XxxConfig` (e.g., `RenameConfig`)

#### Methods and Variables
- **Methods**: `camelCase` with descriptive verbs
- **Variables**: `camelCase` with descriptive nouns
- **Constants**: `SCREAMING_SNAKE_CASE`

#### Package Names
- Lowercase with dots as separators
- Follow the established DDD structure

### Code Formatting

#### Class Structure
```java
package priv.eric.pelee.package.name;

import /* imports */

/**
 * Description: [Brief description in Chinese for this project]
 *
 * @author [AuthorName]
 * @date [YYYY/MM/DD HH:mm]
 */
public class ClassName {
    
    // Fields first (private final where possible)
    private final SomeType fieldName;
    
    // Constructors
    public ClassName(Parameters params) {
        this.fieldName = params.getFieldName();
    }
    
    // Public methods
    public ReturnType publicMethod() {
        // Implementation
    }
    
    // Protected methods
    protected ReturnType protectedMethod() {
        // Implementation
    }
    
    // Private methods
    private ReturnType privateMethod() {
        // Implementation
    }
}
```

#### Method Patterns
```java
// Service methods should be transactional and handle exceptions
@Service
public class SomeService {
    
    public ResultType processSomething(InputType input) {
        try {
            // Validation
            if (input == null) {
                throw new IllegalArgumentException("Input cannot be null");
            }
            
            // Business logic
            SomeResult result = performBusinessLogic(input);
            
            return ResultType.success(result);
        } catch (Exception e) {
            log.error("Processing failed", e);
            throw new BusinessException("Processing failed: " + e.getMessage());
        }
    }
}

// Controller methods should return consistent response format
@RestController
@RequestMapping("/api")
public class SomeController {
    
    @PostMapping("/endpoint")
    public Resp<ResponseType> endpoint(@RequestBody RequestType request) {
        try {
            ResponseType result = service.processRequest(request);
            return Resp.ok(result);
        } catch (Exception e) {
            return Resp.error("Request failed: " + e.getMessage());
        }
    }
}
```

### Annotation Usage

#### Lombok
- Use `@Data` for simple POJOs/config classes
- Use `@Builder` for complex object construction
- Use `@Slf4j` for logging in classes that need it

#### Spring
- `@Service` for business logic services
- `@RestController` and `@RequestMapping` for API controllers
- `@Autowired` for dependency injection
- `@Component` for generic Spring-managed beans

#### Jackson
- Use `@JsonProperty` for custom JSON field mapping
- Use `@JsonCreator` for custom deserialization constructors

### Error Handling

#### Exceptions
- Create specific exception types for different error scenarios
- Use `IllegalArgumentException` for invalid input validation
- Use custom `BusinessException` for business rule violations
- Always log exceptions with context before rethrowing

#### Controller Responses
- Use the `Resp<T>` wrapper class for consistent API responses
- Return `Resp.ok(data)` for successful operations
- Return `Resp.error(message)` for failed operations
- Include error context in error messages

### Plugin Development Guidelines

#### Stage Implementation
```java
public class CustomStage implements Stage {
    
    private final CustomConfig config;
    
    public CustomStage(CustomConfig config) {
        this.config = config;
    }
    
    @Override
    public void process(Event event, StageContext context) {
        try {
            // Transform event data
            Object inputData = event.get();
            Object outputData = transform(inputData);
            
            // Update event with processed data
            event.set(outputData);
            
            // Continue to next stage
            context.next(event);
        } catch (Exception e) {
            throw new StageProcessingException("Custom stage failed", e);
        }
    }
    
    private Object transform(Object data) {
        // Implementation details
        return data;
    }
}
```

#### Plugin Configuration
```java
@Data
public class CustomConfig {
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("configuration")
    private Map<String, Object> configuration;
    
    @JsonProperty("options")
    private CustomOptions options;
}
```

#### Plugin Registration
- Create `META-INF/services/priv.eric.pelee.domain.model.StageFactory` file
- Implement `StageFactory` interface
- Follow existing plugin patterns in `priv.eric.plugin.*`

### JSON Processing

#### Jackson Usage
```java
// Parse JSON to JsonNode
JsonNode node = JsonUtil.parseToJsonNode(jsonString);

// Convert object to JSON
String json = JsonUtil.toJson(object);

// Convert JsonNode to specific type
SomeClass obj = JsonUtil.convertValue(node, SomeClass.class);

// Work with ObjectNode for modifications
ObjectNode objectNode = (ObjectNode) event.get();
objectNode.put("newField", "value");
objectNode.remove("oldField");
```

### Testing Guidelines

#### Test Structure
```java
@ExtendWith(MockitoExtension.class)
class SomeServiceTest {
    
    @Mock
    private SomeRepository repository;
    
    @InjectMocks
    private SomeService service;
    
    @Test
    @DisplayName("Should process valid input successfully")
    void shouldProcessValidInput() {
        // Given
        InputType input = createTestInput();
        ExpectedType expected = createExpectedResult();
        when(repository.someMethod(any())).thenReturn(expected);
        
        // When
        ResultType result = service.processSomething(input);
        
        // Then
        assertThat(result.isSuccess()).isTrue();
        verify(repository).someMethod(input);
    }
    
    @Test
    @DisplayName("Should handle invalid input gracefully")
    void shouldHandleInvalidInput() {
        // Given
        InputType invalidInput = null;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> service.processSomething(invalidInput));
    }
}
```

### Configuration Management

#### Application Configuration
- Server runs on port 9096 (configurable in `application.yml`)
- Pipeline configurations stored in JSON files
- Use Spring's `@ConfigurationProperties` for type-safe configuration

#### Pipeline Configuration
- Pipelines defined in JSON format
- Each pipeline specifies context type and list of processors
- Stage configurations follow the established pattern with type and properties

## Development Workflow

1. **Setup**: Run `./gradlew build` to ensure environment is working
2. **Testing**: Always run `./gradlew test` before committing changes
3. **Code Review**: Ensure code follows the established patterns and conventions
4. **Documentation**: Update relevant documentation when adding new features
5. **Deployment**: Use `./gradlew bootJar` to create executable JAR

## Important Notes

- All comments and descriptions should be in Chinese (following existing project convention)
- Use the existing `Resp<T>` wrapper for all API responses
- Follow the established package structure and naming conventions
- Implement proper exception handling with logging
- Create comprehensive tests for new functionality
- Use Jackson utilities from `priv.eric.pelee.infrastructure.util.JsonUtil` for JSON operations