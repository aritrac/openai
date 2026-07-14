# Spring AI OpenAI Integration

A Spring Boot application that integrates with OpenAI's language models through Spring AI framework to provide intelligent chatbot services for various organizational functions.

## Overview

This project demonstrates how to build AI-powered assistant applications using:
- **Spring Boot 4.1.0** - Modern Java web framework
- **Spring AI 2.0.0** - AI framework for Java developers
- **OpenAI API** - Powerful language models for natural language processing
- **Java 25** - Latest Java features

## Features

- **IT Helpdesk Assistant** - Specialized chatbot for IT support queries (password resets, account unlocking, IT policies)
- **HR Assistant** - AI-powered assistant for HR-related questions (leave policies, benefits, code of conduct)
- **Extensible Architecture** - Easy to add more specialized assistants using the controller and configuration pattern
- **Custom System Prompts** - Each assistant has tailored instructions to maintain focus on specific domains

## Prerequisites

- Java 25 or higher
- Maven 3.6.0 or higher
- OpenAI API key

## Project Structure

```
openai/
├── src/
│   ├── main/
│   │   ├── java/com/binarylife/openai/
│   │   │   ├── SpringAIApplication.java          # Main Spring Boot application
│   │   │   ├── controller/
│   │   │   │   ├── ChatController.java            # IT Helpdesk API endpoint
│   │   │   │   ├── MultiModelChatController.java  # Multi-model support (optional)
│   │   │   │   └── PromptTemplateController.java  # Prompt template utilities
│   │   │   └── config/
│   │   │       └── ChatClientConfig.java          # Spring AI ChatClient configuration
│   │   └── resources/
│   │       ├── application.properties             # Application configuration
│   │       └── promptTemplates/                   # Reusable prompt templates
│   └── test/                                      # Test suite
├── pom.xml                                        # Maven configuration
└── mvnw, mvnw.cmd                                 # Maven wrapper scripts
```

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/aritrac/openai.git
cd openai
```

### 2. Configure OpenAI API Key

Set your OpenAI API key as an environment variable:

**Windows (PowerShell):**
```powershell
$env:SPRING_AI_OPENAI_API_KEY="your-api-key-here"
```

**Linux/macOS:**
```bash
export SPRING_AI_OPENAI_API_KEY="your-api-key-here"
```

Or add it to `src/main/resources/application.properties`:
```properties
spring.ai.openai.api-key=your-api-key-here
```

### 3. Build the Project

```bash
mvn clean install
```

Or use the Maven wrapper:

**Windows:**
```cmd
mvnw.cmd clean install
```

**Linux/macOS:**
```bash
./mvnw clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### IT Helpdesk Chat
- **Endpoint:** `GET /api/chat`
- **Parameter:** `message` (query parameter)
- **Example:**
  ```
  GET http://localhost:8080/api/chat?message=How do I reset my password?
  ```
- **Response:** AI-generated response from IT Helpdesk Assistant

### HR Chat
- **Endpoint:** Uses default ChatClient bean configuration
- **Note:** Can be exposed through a controller by implementing the provided configuration

## API Examples

### Using cURL

```bash
# IT Helpdesk Query
curl "http://localhost:8080/api/chat?message=How%20do%20I%20unlock%20my%20account?"

# HR Query
curl "http://localhost:8080/api/chat?message=What%20is%20the%20leave%20policy?"
```

### Using JavaScript/Node.js

```javascript
const message = "I forgot my password";
const response = await fetch(`http://localhost:8080/api/chat?message=${encodeURIComponent(message)}`);
const result = await response.text();
console.log(result);
```

## System Prompts

The application uses custom system prompts to keep assistants focused on their domains:

### IT Helpdesk Assistant Scope
- Password resets
- Account unlocking
- IT policies
- IT-related inquiries

### HR Assistant Scope
- Leave policies
- Working hours
- Benefits
- Code of conduct
- HR policies and procedures

## Configuration

### Spring AI Configuration (`ChatClientConfig.java`)

The `ChatClientConfig` class configures:
- Default system messages for assistants
- ChatClient beans for dependency injection
- Default user interactions

### Application Properties

Edit `src/main/resources/application.properties` to customize:
```properties
# OpenAI Configuration
spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY}
spring.ai.openai.base-url=https://api.openai.com/v1
spring.ai.openai.model=gpt-4

# Server Configuration
server.port=8080
```

## Development

### Adding a New Assistant

1. **Create a new Controller**
   ```java
   @RestController
   @RequestMapping("/api/newassistant")
   public class NewAssistantController {
       private final ChatClient chatClient;
       
       public NewAssistantController(ChatClient chatClient) {
           this.chatClient = chatClient;
       }
       
       @GetMapping("/chat")
       public String chat(@RequestParam("message") String message) {
           return chatClient.prompt()
               .system("Your custom system prompt here")
               .user(message)
               .call().content();
       }
   }
   ```

2. **Configure in ChatClientConfig** (if needed for multiple clients)

3. **Test the new endpoint**

### Running Tests

```bash
mvn test
```

## Multi-Model Support

The project includes a `MultiModelChatController` (currently commented out) for supporting multiple AI models:
- OpenAI
- Ollama (local LLMs)
- AWS Bedrock

To enable multi-model support, uncomment the code in:
- `MultiModelChatController.java`
- Add required dependencies in `pom.xml`

## Troubleshooting

### Common Issues

**Issue: API Key not recognized**
- Verify the API key is correctly set in environment variables
- Check that the key has appropriate permissions in OpenAI dashboard

**Issue: Connection timeout**
- Ensure internet connectivity
- Verify OpenAI API is accessible
- Check firewall settings

**Issue: 401 Unauthorized**
- Validate your OpenAI API key
- Ensure the API key is active in your OpenAI account

## Dependencies

- `spring-boot-starter-webmvc` - Web framework
- `spring-ai-starter-model-openai` - OpenAI integration
- `spring-boot-devtools` - Development tools
- `spring-boot-starter-webmvc-test` - Testing support

## Technologies Used

- Java 25
- Spring Boot 4.1.0
- Spring AI 2.0.0
- OpenAI API
- Maven

## License

[Add your license information here]

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues, questions, or suggestions, please open an issue in the repository.

## Acknowledgments

- Spring Framework and Spring Boot teams
- Spring AI project contributors
- OpenAI for their powerful language models
