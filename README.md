# 🎓 Shadowise API

AI-powered intelligent learning platform RESTful API service.

## 📋 About The Project

Shadowise API is a Spring Boot application that automatically generates quiz questions, flashcards, and summaries from user-uploaded PDF documents using artificial intelligence. It is designed to optimize the learning processes of students and educators. I developed this project within the scope of BTK Hackathon 2025.

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based secure authentication
- User registration and login system
- Refresh token support
- Role-based access control

### 📁 Project Management
- Create and manage projects
- File upload and organization
- Project-based content organization
- Dashboard statistics

### 🤖 AI-Powered Content Generation
- **PDF Processing**: Upload and process PDF files
- **Automatic Summarization**: Generate AI-powered summaries from documents
- **Quiz Generation**: Create multiple-choice questions
- **Flashcard Creation**: Generate question-answer cards
- Customizable parameters (number of questions, word limit, etc.)

### 📊 Content Management
- Quiz management and score tracking
- Flashcard collections
- Note-taking system
- File download and viewing

## 🛠️ Technologies

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.2.4** - Application framework
- **Spring Security** - Security and authentication
- **Spring Data MongoDB** - Database access
- **Spring Cloud OpenFeign** - Microservice communication

### Database
- **MongoDB** - NoSQL document database

### Libraries & Tools
- **JWT (JSON Web Token)** - Token-based authentication
- **Lombok** - Code optimization
- **MapStruct** - DTO mappings
- **SpringDoc OpenAPI** - API documentation (Swagger UI)
- **Docker** - Containerization

## 📦 Installation

### Requirements
- Java 21 or higher
- Maven 3.6+
- MongoDB 4.0+
- Docker (optional)

## 🚀 Usage

### API Documentation
After running the application, you can explore the API via Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

### Main Endpoints

#### Authentication
- `POST /api/auth/sign-up` - Register new user
- `POST /api/auth/sign-in` - User login
- `POST /api/auth/refresh-token` - Refresh token
- `POST /api/auth/logout` - Logout

#### Project Operations
- `GET /api/projects/my-projects` - List user's projects
- `POST /api/projects/create-with-files` - Create project with files
- `GET /api/projects/{id}` - Get project details
- `PUT /api/projects/{id}` - Update project
- `DELETE /api/projects/{id}` - Delete project

#### AI Operations
- `POST /api/ai/upload-pdf` - Upload PDF
- `POST /api/ai/generate-summary` - Generate summary
- `POST /api/ai/generate-questions` - Generate quiz questions
- `POST /api/ai/generate-flashcards` - Generate flashcards

#### Quiz Operations
- `GET /api/quiz` - List quizzes
- `POST /api/quiz` - Create quiz
- `GET /api/quiz/{id}` - Get quiz details
- `PUT /api/quiz/{id}/score` - Update score

#### Flashcard Operations
- `GET /api/flashcard` - List flashcards
- `POST /api/flashcard` - Create flashcard
- `GET /api/flashcard/{id}` - Get flashcard details

## 📂 Project Structure

```
src/main/java/tr/shadowise_api/
├── client/              # External API clients
├── config/              # Configuration classes
├── controller/          # REST Controllers
├── core/                # Core response structures
├── dto/                 # Data Transfer Objects
├── entity/              # MongoDB Entities
├── exception/           # Custom exceptions
├── payload/             # Request/Response payloads
├── repository/          # MongoDB Repositories
├── service/             # Business logic services
└── utils/               # Utility classes
```

## 🔒 Security

- JWT-based authentication
- Password encryption (BCrypt)
- CORS configuration
- Role-based authorization
- Data security with soft delete

## 🌍 Environment Profiles

The application includes profiles for different environments:
- **dev** - Development environment
- **prod** - Production environment

Profile selection:
```bash
java -jar app.jar --spring.profiles.active=prod
```

## 📞 Contact

- **Developer**: Arif Özcan
- **Email**: zcanarif@gmail.com
- **GitHub**: [@arifozcan35](https://github.com/arifozcan35)
