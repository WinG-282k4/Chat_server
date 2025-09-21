# Chatserver Project

## Overview
A Spring Boot-based chat server supporting authentication, chat rooms, messaging, and user management.

## Project Structure

```
chatserver/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/chatserver/
│   │   │       ├── ChatserverApplication.java
│   │   │       ├── authentication/         # Auth logic (controllers, services, models)
│   │   │       ├── chatroom/               # Chat room logic
│   │   │       ├── Chatroomparticipant/    # Chatroom participant management
│   │   │       ├── common/                 # Common utilities and responses
│   │   │       ├── config/                 # Configuration classes
│   │   │       ├── message/                # Messaging logic
│   │   │       ├── message_interaction/    # Message interaction logic
│   │   │       ├── security/               # Security (JWT, filters, config)
│   │   │       └── user/                   # User management
│   │   ├── resources/
│   │   │   ├── application.properties      # App configuration
│   │   │   ├── static/                     # Static resources
│   │   │   └── templates/                  # View templates
│   └── test/
│       └── java/
│           └── com/example/chatserver/
│               └── ChatserverApplicationTests.java
├── pom.xml                                 # Maven build file
├── README.md                               # Project documentation
├── HELP.md                                 # Spring Boot help
```

## How to Build & Run

1. **Build:**
   ```
   mvn clean install
   ```
2. **Run:**
   ```
   mvn spring-boot:run
   ```

## Main Features
- User registration & authentication (JWT)
- Chat room creation & management
- Messaging between users
- Message interactions (like, reply, etc.)
- Role-based access control

## Configuration
Edit `src/main/resources/application.properties` for database and JWT settings.

## API Endpoints
- `/api/auth/*` — Authentication
- `/api/user/*` — User management
- `/api/chatroom/*` — Chat room operations
- `/api/message/*` — Messaging

## Contributing
Pull requests and issues are welcome!

## License
MIT

