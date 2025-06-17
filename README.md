# Chatserver

A simple chat server application built with Java, Spring Boot, and JPA.

## Features
- User authentication and management
- Real-time chat between users
- Support for chat rooms
- Message reactions and replies
- Message status tracking (sent, delivered, seen)

## Project Structure
```
src/
  main/
    java/
      com/example/chatserver/
        controller/      # REST controllers
        model/           # JPA entities (user, message, chatroom)
        repository/      # Spring Data JPA repositories
        service/         # Business logic
    resources/
      application.properties
      static/
      templates/
  test/
    java/
      com/example/chatserver/
```

## Getting Started

### Prerequisites
- Java 17 or newer
- Maven 3.6+

### Build and Run
1. Clone the repository:
   ```sh
   git clone <repo-url>
   cd chatserver
   ```
2. Build the project:
   ```sh
   ./mvnw clean install
   ```
3. Run the application:
   ```sh
   ./mvnw spring-boot:run
   ```

### Configuration
Edit `src/main/resources/application.properties` to configure database and server settings.

## Usage
- Access the API endpoints via Postman or a frontend client.
- Default port: `8080`

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License.

