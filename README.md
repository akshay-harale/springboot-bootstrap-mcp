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

## Quick Start

### 1. Download the JAR

Go to the [Releases page](../../releases/latest) and download `app-bootstrap-0.0.1-SNAPSHOT.jar`

Save it to: `C:/mcp-servers/app-bootstrap.jar` (Windows) or `~/mcp-servers/app-bootstrap.jar` (Mac/Linux)

### 2. Configure Your MCP Client

Choose your client below and add the configuration:

#### GitHub Copilot (VS Code)

Edit: `%APPDATA%\Code\User\mcp.json` (Windows) or `~/Library/Application Support/Code/User/mcp.json` (Mac/Linux)

```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": ["-jar", "C:/mcp-servers/app-bootstrap.jar"]
    }
  }
}
```

#### Claude Desktop

Edit: `%APPDATA%\Claude\claude_desktop_config.json` (Windows) or `~/Library/Application Support/Claude/claude_desktop_config.json` (Mac)

```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": ["-jar", "C:/mcp-servers/app-bootstrap.jar"]
    }
  }
}
```

#### Other MCP Clients (Cline, Zed, Cody, Continue.dev)

```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": ["-jar", "/path/to/app-bootstrap.jar"]
    }
  }
}
```

**Mac/Linux path example**: `/Users/username/mcp-servers/app-bootstrap.jar`

### 3. Restart Your Client

Restart VS Code, Claude Desktop, or your MCP client to load the server.

### 4. Start Using

Try prompts like:
```
Create a Spring Boot REST API with PostgreSQL and JPA, save to C:/projects/myapi.zip
```

---

## Detailed Documentation

### Alternative Installation Methods

#### Option 1: Download Pre-built Release (Recommended - Already covered in Quick Start above)

See the [Quick Start](#quick-start) section above for the easiest way to get started.

#### Option 2: Build from Source

If you want to build from source or contribute to the project:

#### Prerequisites for Building
- Java 21 or higher
- Gradle (included via wrapper)

#### Build Steps

```bash
# Windows
.\gradlew.bat clean bootJar

# Linux/Mac
./gradlew clean bootJar
```

The built JAR file will be located at: `build/libs/app-bootstrap-0.0.1-SNAPSHOT.jar`

## Configuration

### Server Configuration (Optional)

The server is pre-configured and ready to use. Default settings are in `src/main/resources/application.properties`:

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

**Note**: An alternative WebFlux configuration is available at `src/main/resources/application.json` for reactive applications (requires building from source).

---

## Client-Specific Configuration Details

The Quick Start section above covers most use cases. This section provides additional details and troubleshooting.

### GitHub Copilot (VS Code) - Extended

Configuration file locations:
- **Windows**: `%APPDATA%\Code\User\mcp.json`
- **macOS**: `~/Library/Application Support/Code/User/mcp.json`
- **Linux**: `~/.config/Code/User/mcp.json`

**Complete configuration example:**

```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": ["-jar", "C:/mcp-servers/app-bootstrap.jar"],
      "env": {}
    }
  }
}
```

**Mac/Linux example:**
```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": ["-jar", "/Users/username/mcp-servers/app-bootstrap.jar"]
    }
  }
}

#### Step 3: Restart VS Code

#### Step 4: Use with Copilot Chat

Open Copilot Chat and use prompts like:
```
Create a Spring Boot REST API with PostgreSQL and JPA, save to C:/projects/myapi.zip
```

### 2. Claude Desktop

#### Step 1: Get the JAR file

**Option A: Download pre-built release (Recommended)**
- Download from [Releases page](../../releases/latest)
- Save to a location like `C:/mcp-servers/app-bootstrap-0.0.1-SNAPSHOT.jar` (Windows)
- Or `~/mcp-servers/app-bootstrap-0.0.1-SNAPSHOT.jar` (macOS/Linux)

**Option B: Build from source**
```bash
.\gradlew.bat clean bootJar  # Windows
./gradlew clean bootJar      # Linux/Mac
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
        "C:/mcp-servers/app-bootstrap-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

**macOS/Linux example:**
```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": [
        "-jar",
        "/Users/username/mcp-servers/app-bootstrap-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

After restarting Claude Desktop, the tools will be available.

### Other MCP Clients - Extended

Configuration for Cline, Zed Editor, Sourcegraph Cody, Continue.dev, and other MCP-compatible clients:

```json
{
  "mcpServers": {
    "springboot-bootstrap": {
      "command": "java",
      "args": ["-jar", "/path/to/app-bootstrap.jar"]
    }
  }
}
```

Replace `/path/to/app-bootstrap.jar` with your actual file path.

---

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

### Automated Releases

Every push to the `main` branch automatically triggers a GitHub Actions workflow that:
1. Builds the project with Gradle
2. Creates a timestamped release tag
3. Publishes a new GitHub release with the pre-built JAR file

**No manual building needed for end users!** Just download from the [Releases page](../../releases/latest).

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
3. Push to main branch (automatic build and release)
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
