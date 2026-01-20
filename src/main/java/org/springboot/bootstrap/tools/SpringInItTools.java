package org.springboot.bootstrap.tools;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * MCP Tools for bootstrapping Spring Boot projects using Spring Initializr.
 * 
 * Workflow:
 * 1. First, call getSpringBootInitDetails() to get available options (boot versions, dependencies, etc.)
 * 2. Then, call downloadSpringBootProject() with desired configuration to generate and download the project
 */
@Component
public class SpringInItTools {

    private Logger logger = Logger.getLogger(SpringInItTools.class.getName());

    private final RestClient restClient;

    public SpringInItTools(RestClient restClient) {
        this.restClient = restClient;
        logger.info("SpringInItTools initialized with RestClient");
    }

    /**
     * Step 1: Get available Spring Boot initialization options from start.spring.io
     * 
     * This tool retrieves metadata about all available configuration options for creating
     * a Spring Boot project. Use this FIRST to discover valid values for parameters.
     * 
     * Returns JSON containing:
     * - type: Project types (maven-project, gradle-project, gradle-project-kotlin, maven-project)
     * - language: Languages (java, kotlin, groovy)
     * - bootVersion: Available Spring Boot versions (e.g., 3.5.9, 3.4.2, etc.)
     * - dependencies: All available dependencies with IDs, names, and descriptions
     * - javaVersion: Supported Java versions (17, 21, 23, etc.)
     * - packaging: Packaging types (jar, war)
     * - groupId: Default group ID pattern
     * - artifactId: Default artifact ID pattern
     * - name: Default project name
     * - description: Default project description
     * - packageName: Default package name pattern
     * - configurationFileFormat: Configuration file formats (properties, yaml)
     */
    @McpTool(name = "getSpringBootInitDetails", 
        description = "Provide details about Spring Boot initialization."+
        "This tools returns the options that we select when we want to bootstrap springboot project,"+
        "which includes:"+
        "artifactId:\r\n" + 
        "bootVersion:\r\n" +
        "configurationFileFormat:\r\n" + 
        "dependencies:\r\n" + 
        "description:\r\n" + 
        "groupId: \r\n" + 
        "javaVersion:\r\n" + 
        "language:\r\n" + 
        "name:\r\n" + 
        "packageName:\r\n" + 
        "packaging:\r\n" + 
        "type:\r\n" +
        "Client can use this information to generate a customized Spring Boot project.")
    public String getSpringBootInitDetails() {
        logger.info("Fetching Spring Boot initialization details from start.spring.io");
        try {
            var result = restClient.get()
                .uri("https://start.spring.io/metadata/client")
                .retrieve();
            String body = result.body(String.class);
            logger.info("Successfully fetched Spring Boot initialization details");
            return body;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching Spring Boot initialization details", e);
            return "Error fetching details: " + e.getMessage();
        }
    }

    /**
     * Step 2: Download a customized Spring Boot project as a ZIP file or extract it to a directory
     * 
     * This tool generates and downloads a Spring Boot project from start.spring.io based on
     * user specifications. Call getSpringBootInitDetails() FIRST to get valid parameter values.
     * 
     * Example API call:
     * https://start.spring.io/starter.zip?type=gradle-project&language=java&bootVersion=3.5.9
     * &groupId=com.example&artifactId=demo&name=demo&description=Demo%20project%20for%20Spring%20Boot
     * &packageName=com.example.demo&packaging=jar&javaVersion=17&configurationFileFormat=properties
     * &dependencies=web,postgresql
     * 
     * @param type Project build tool type. Common values: "gradle-project", "maven-project", "gradle-project-kotlin"
     * @param language Programming language. Values: "java", "kotlin", "groovy"
     * @param bootVersion Spring Boot version. IMPORTANT: Use EXACT version from getSpringBootInitDetails().
     *                    Spring Boot 3.x uses semantic versioning (e.g., "3.5.9", "3.4.2").
     *                    Spring Boot 2.x requires .RELEASE suffix (e.g., "2.7.18.RELEASE").
     *                    Always call getSpringBootInitDetails() first to get the correct format.
     * @param groupId Maven group ID. Example: "com.example"
     * @param artifactId Maven artifact ID (project name). Example: "demo"
     * @param name Project display name. Example: "Demo"
     * @param description Project description. Example: "Demo project for Spring Boot"
     * @param packageName Base package name. Example: "com.example.demo"
     * @param packaging Package type. Values: "jar", "war"
     * @param javaVersion Java version. Common values: "17", "21", "23"
     * @param dependencies Comma-separated dependency IDs. Example: "web,data-jpa,postgresql,lombok"
     * @param configurationFileFormat Config file format. Values: "properties", "yaml"
     * @param downloadPath Absolute path where the ZIP file should be saved OR directory where files should be extracted. 
     *                     If ends with .zip, saves as ZIP file. Otherwise, extracts to that directory.
     *                     Examples: "C:/projects/my-app.zip" (saves ZIP) or "C:/projects/my-app" (extracts files)
     * @return Success message with file path or error message
     */
    @McpTool(name = "downloadSpringBootProject",
        description = "Download a customized Spring Boot project from start.spring.io. " +
        "This tool generates a complete Spring Boot project with specified configuration and dependencies. " +
        "IMPORTANT: Call getSpringBootInitDetails() FIRST to discover valid parameter values." +
        "Use exact parameter values that you get from getSpringBootInitDetails(). "+
        "Parameters: " +
        "type (required): Project build tool - 'gradle-project' or 'maven-project'. " +
        "language (required): Programming language - 'java', 'kotlin', or 'groovy'. " +
        "bootVersion (required): EXACT Spring Boot version from getSpringBootInitDetails(). " +
        "CRITICAL: Spring Boot 3.x uses format '3.5.9' (no suffix). Spring Boot 2.x uses format '2.7.18.RELEASE' (with .RELEASE suffix). " +
        "Do NOT guess the version format - always get it from getSpringBootInitDetails() first. " +
        "groupId (required): Maven group ID like 'com.example'. " +
        "artifactId (required): Project/artifact ID like 'demo'. " +
        "name (required): Project display name like 'Demo Application'. " +
        "description (optional): Project description. " +
        "packageName (required): Base package name like 'com.example.demo'. " +
        "packaging (required): 'jar' or 'war'. " +
        "javaVersion (required): Java version like '17', '21', or '23'. " +
        "dependencies (required): Comma-separated dependency IDs from metadata like 'web,data-jpa,lombok'. " +
        "configurationFileFormat (optional): 'properties' or 'yaml', defaults to 'properties'. " +
        "downloadPath (required): Path where project should be saved. If ends with '.zip', downloads as ZIP file. " +
        "Otherwise extracts files directly to that directory. Examples: 'C:/projects/app.zip' (ZIP) or 'C:/projects/app' (extracted). " +
        "Returns: Success message with saved file path or error details.")
    public String downloadSpringBootProject(
            String type,
            String language,
            String bootVersion,
            String groupId,
            String artifactId,
            String name,
            String description,
            String packageName,
            String packaging,
            String javaVersion,
            String dependencies,
            String configurationFileFormat,
            String downloadPath) {
        
        logger.info("Downloading Spring Boot project with configuration:");
        logger.info("Type: " + type + ", Language: " + language + ", Boot Version: " + bootVersion);
        logger.info("GroupId: " + groupId + ", ArtifactId: " + artifactId);
        logger.info("Dependencies: " + dependencies);
        logger.info("Download Path: " + downloadPath);

        try {
            // Build the URI with query parameters
            StringBuilder uriBuilder = new StringBuilder("https://start.spring.io/starter.zip?");
            uriBuilder.append("type=").append(type);
            uriBuilder.append("&language=").append(language);
            uriBuilder.append("&bootVersion=").append(bootVersion);
            uriBuilder.append("&groupId=").append(groupId);
            uriBuilder.append("&artifactId=").append(artifactId);
            uriBuilder.append("&name=").append(name);
            uriBuilder.append("&description=").append(description != null ? description : "Spring Boot Application");
            uriBuilder.append("&packageName=").append(packageName);
            uriBuilder.append("&packaging=").append(packaging);
            uriBuilder.append("&javaVersion=").append(javaVersion);
            if (dependencies != null && !dependencies.isEmpty()) {
                uriBuilder.append("&dependencies=").append(dependencies);
            }
            if (configurationFileFormat != null && !configurationFileFormat.isEmpty()) {
                uriBuilder.append("&configurationFileFormat=").append(configurationFileFormat);
            }

            String uri = uriBuilder.toString();
            logger.info("Request URI: " + uri);

            // Download the ZIP file
            byte[] zipBytes = restClient.get()
                .uri(uri)
                .retrieve()
                .body(byte[].class);

            if (zipBytes == null || zipBytes.length == 0) {
                return "Error: Received empty response from start.spring.io";
            }

            // Check if downloadPath ends with .zip - if so, save as ZIP, otherwise extract
            boolean saveAsZip = downloadPath.toLowerCase().endsWith(".zip");
            
            if (saveAsZip) {
                // Save as ZIP file
                Path path = Paths.get(downloadPath);
                Files.createDirectories(path.getParent());

                try (FileOutputStream fos = new FileOutputStream(downloadPath)) {
                    fos.write(zipBytes);
                }

                logger.info("Successfully downloaded Spring Boot project to: " + downloadPath);
                return "Successfully downloaded Spring Boot project (" + zipBytes.length + " bytes) to: " + downloadPath + 
                       "\nProject details: " + name + " (" + artifactId + ") with Spring Boot " + bootVersion +
                       "\nDependencies: " + dependencies;
            } else {
                // Extract ZIP to directory
                Path extractPath = Paths.get(downloadPath);
                Files.createDirectories(extractPath);
                
                int filesExtracted = extractZipToDirectory(zipBytes, extractPath);
                
                logger.info("Successfully extracted Spring Boot project to: " + downloadPath);
                return "Successfully extracted Spring Boot project (" + filesExtracted + " files) to: " + downloadPath + 
                       "\nProject details: " + name + " (" + artifactId + ") with Spring Boot " + bootVersion +
                       "\nDependencies: " + dependencies +
                       "\n\nYou can now open this directory in your IDE and start developing!";
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Error downloading Spring Boot project", e);
            return "Error downloading project: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error downloading Spring Boot project", e);
            return "Error downloading project: " + e.getMessage();
        }
    }

    /**
     * Extracts a ZIP file from byte array to a directory
     * 
     * @param zipBytes The ZIP file content as byte array
     * @param targetDir The directory where files should be extracted
     * @return Number of files extracted
     * @throws IOException If extraction fails
     */
    private int extractZipToDirectory(byte[] zipBytes, Path targetDir) throws IOException {
        int filesExtracted = 0;
        
        try (ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
             ZipInputStream zis = new ZipInputStream(bais)) {
            
            ZipEntry entry;
            byte[] buffer = new byte[8192];
            
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = targetDir.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                    logger.fine("Created directory: " + filePath);
                } else {
                    // Ensure parent directories exist
                    Files.createDirectories(filePath.getParent());
                    
                    // Extract file
                    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    
                    filesExtracted++;
                    logger.fine("Extracted file: " + filePath);
                }
                
                zis.closeEntry();
            }
        }
        
        logger.info("Total files extracted: " + filesExtracted);
        return filesExtracted;
    }
}
