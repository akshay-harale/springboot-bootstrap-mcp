# Spring Boot Bootstrap MCP Server

A Model Context Protocol (MCP) server for bootstrapping Spring Boot projects using Spring Initializr. This server provides AI assistants with tools to discover available Spring Boot configurations and generate customized project templates.

## Features

### Available Tools

1. **getSpringBootInitDetails**
   - Retrieves all available configuration options from Spring Initializr
   - Returns available Spring Boot versions, dependencies, Java versions, and more
   - Use this first to discover valid parameter values

2. **downloadSpringBootProject**
   - Downloads a fully configured Spring Boot project as a ZIP file
   - Supports customization of build tool, language, dependencies, and more
   - Automatically creates parent directories if needed

## Prerequisites

- Java 21 or higher
- Gradle (included via wrapper)
- Internet connection (to access start.spring.io)

## Building the Project

```bash
# Windows
.\gradlew.bat build

# Linux/Mac
./gradlew build
```

The built JAR file will be located at: `build/libs/app-bootstrap-0.0.1-SNAPSHOT.jar`

## Configuration

### Application Properties

Located at `src/main/resources/application.properties`:

```properties
spring.main.web-application-type=none
spring.main.banner-mode=off
spring.application.name=app-bootstrap

spring.ai.mcp.server.name=spring-bootstrap-mcp
spring.ai.mcp.server.version=0.0.1
spring.ai.mcp.server.stdio=true
spring.ai.mcp.server.type=SYNC
spring.ai.mcp.server.annotation-scanner.enabled=true

logging.file.name=./mcp-springboot-bootstrap-stdio-server.log
```

### Alternative JSON Configuration

A sample WebFlux configuration is available at `src/main/resources/application.json` for reactive applications.

## Running with MCP Clients

### 1. GitHub Copilot (VS Code)

#### Step 1: Build the JAR file
```bash
.\gradlew.bat bootJar
```

#### Step 2: Configure MCP in VS Code

Edit your MCP configuration file:
- **Windows**: `%APPDATA%\Code\User\mcp.json`
- **macOS**: `~/Library/Application Support/Code/User/mcp.json`
- **Linux**: `~/.config/Code/User/mcp.json`

Add this configuration:

```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": [
        "-jar",
        "C:/code/springboot-bootstrap-mcp/build/libs/app-bootstrap-0.0.1-SNAPSHOT.jar"
      ],
      "env": {}
    }
  }
}
```

**Important**: Update the JAR path to match your actual project location.

#### Step 3: Restart VS Code

#### Step 4: Use with Copilot Chat

Open Copilot Chat and use prompts like:
```
Create a Spring Boot REST API with PostgreSQL and JPA, save to C:/projects/myapi.zip
```

### 2. Claude Desktop

#### Step 1: Build the JAR file
```bash
.\gradlew.bat bootJar
```

#### Step 2: Configure Claude Desktop

Edit the Claude Desktop configuration file:
- **Windows**: `%APPDATA%\Claude\claude_desktop_config.json`
- **macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`

Add this configuration:

```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": [
        "-jar",
        "C:/code/springboot-bootstrap-mcp/build/libs/app-bootstrap-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

**Important**: Update the JAR path to match your actual project location.

#### Step 3: Restart Claude Desktop

The Spring Boot Bootstrap tools will be available in Claude Desktop.

### 3. Other MCP Clients

Any MCP-compatible client can use this server. The general configuration pattern is:

```json
{
  "command": "java",
  "args": [
    "-jar",
    "/path/to/app-bootstrap-0.0.1-SNAPSHOT.jar"
  ]
}
```

#### Supported MCP Clients:
- **Cline** (VS Code Extension)
- **Zed Editor**
- **Sourcegraph Cody**
- **Continue.dev**
- Any client supporting MCP over STDIO

## Usage Examples

### Example 1: Basic Web Application

```
Create a basic Spring Boot web application with Java 21 and save it to C:/projects/demo.zip
```

The AI will:
1. Call `getSpringBootInitDetails()` to get available options
2. Select appropriate defaults (Spring Web, latest Spring Boot version, etc.)
3. Call `downloadSpringBootProject()` with the configuration
4. Save the ZIP file to the specified location

### Example 2: Microservice with Database

```
I need a Spring Boot microservice with:
- Spring Boot 3.5.9
- Java 21, Gradle
- Dependencies: Web, Data JPA, PostgreSQL, Lombok, Actuator
- Group: com.mycompany
- Artifact: user-service
- Save to: C:/microservices/user-service.zip
```

### Example 3: Reactive Application

```
Create a Spring Boot WebFlux application with MongoDB and save to C:/projects/reactive-api.zip
```

## Tool Parameters

### getSpringBootInitDetails

No parameters required. Returns JSON with all available configuration options.

### downloadSpringBootProject

| Parameter | Required | Description | Example |
|-----------|----------|-------------|---------|
| `type` | Yes | Build tool type | `gradle-project`, `maven-project` |
| `language` | Yes | Programming language | `java`, `kotlin`, `groovy` |
| `bootVersion` | Yes | Spring Boot version | `3.5.9`, `3.4.2` |
| `groupId` | Yes | Maven group ID | `com.example` |
| `artifactId` | Yes | Project/artifact ID | `demo` |
| `name` | Yes | Project display name | `Demo Application` |
| `description` | No | Project description | `Demo project for Spring Boot` |
| `packageName` | Yes | Base package name | `com.example.demo` |
| `packaging` | Yes | Package type | `jar`, `war` |
| `javaVersion` | Yes | Java version | `17`, `21`, `23` |
| `dependencies` | Yes | Comma-separated dependency IDs | `web,data-jpa,postgresql` |
| `configurationFileFormat` | No | Config file format | `properties`, `yaml` |
| `downloadPath` | Yes | Absolute path for ZIP file | `C:/projects/app.zip` |

## Common Dependencies

Some frequently used dependency IDs:

- `web` - Spring Web (REST APIs)
- `webflux` - Spring WebFlux (Reactive)
- `data-jpa` - Spring Data JPA
- `data-mongodb` - Spring Data MongoDB
- `postgresql` - PostgreSQL Driver
- `mysql` - MySQL Driver
- `h2` - H2 Database
- `lombok` - Lombok
- `actuator` - Spring Boot Actuator
- `security` - Spring Security
- `validation` - Validation
- `cloud-config-client` - Spring Cloud Config Client
- `cloud-eureka-client` - Eureka Discovery Client

Use `getSpringBootInitDetails()` to get the complete list with descriptions.

## Troubleshooting

### JAR file not found
```bash
# Rebuild the project
.\gradlew.bat clean bootJar
```

### Connection errors
- Ensure you have internet access to reach `start.spring.io`
- Check firewall settings

### Tools not appearing in MCP client
- Verify the JAR path in your MCP configuration is correct
- Restart the MCP client completely
- Check logs at `./mcp-springboot-bootstrap-stdio-server.log`

### Permission errors when downloading
- Ensure the download directory exists and you have write permissions
- Use absolute paths (e.g., `C:/projects/app.zip` not `./app.zip`)

## Development

### Project Structure

```
src/main/java/org/springboot/bootstrap/
├── AppBootstrapApplication.java          # Main application
├── config/
│   └── RestClientConfig.java            # RestClient configuration
└── tools/
    └── SpringInItTools.java             # MCP tools implementation
```

### Adding New Tools

1. Add a new method in `SpringInItTools.java`
2. Annotate with `@McpTool` with name and description
3. Rebuild the project
4. Tools are automatically discovered via annotation scanning

### Testing

```bash
# Run tests
.\gradlew.bat test

# Run application locally (for testing)
.\gradlew.bat bootRun
```

## Technology Stack

- **Spring Boot**: 3.5.9
- **Spring AI MCP Server**: 1.1.2
- **Java**: 21
- **Build Tool**: Gradle
- **HTTP Client**: Apache HttpClient 5

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is provided as-is for demonstration purposes.

## Support

For issues or questions:
- Check the troubleshooting section above
- Review logs at `./mcp-springboot-bootstrap-stdio-server.log`
- Ensure you're using Java 21 or higher

## Related Links

- [Spring Initializr](https://start.spring.io)
- [Model Context Protocol Specification](https://modelcontextprotocol.io)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

---

**Built with ❤️ using Spring Boot and Spring AI MCP Server**
