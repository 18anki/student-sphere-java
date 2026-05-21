# 🎓 StudentsSphere

StudentsSphere is a Spring Boot based student management system designed to manage student records, attendance, course workflows, and academic processes efficiently.

The application provides backend functionalities using REST APIs, MySQL database integration, and scalable architecture for academic management systems.

---

## 🚀 Features

- Student Management
- Attendance Management
- Course & Subject Management
- Academic Workflow Handling
- REST API Integration
- Database Management using MySQL
- Docker-based Local Setup
- Profile-based Configuration
- CRUD Operations
- Dynamic Data Seeding

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- H2 Database
- Docker
- Maven
- REST APIs
- HikariCP
- Git & GitHub

---

## 📡 Sample APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /students | Fetch all students |
| POST | /students | Add new student |
| PUT | /students/{id} | Update student |
| DELETE | /students/{id} | Delete student |

---

# ⚙️ Local MySQL Setup

This project defaults to H2 for development. To run the application against MySQL locally, follow these steps.

---

## 📋 Prerequisites

- Docker (for local MySQL) or an existing MySQL server
- Java 17
- Maven

---

## 1️⃣ Start MySQL with Docker Compose

From the project root run:

```powershell
# Start MySQL container in background
docker-compose up -d
```

This uses the provided `docker-compose.yml` which creates a MySQL 8.0 container with:

- Database: `studentsphere`
- User: `studentsphere_user`
- Password: `studentsphere_pass`
- Root Password: `rootpass`

---

## 2️⃣ Run the Spring Boot Application with MySQL Profile

### Using Maven

```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### Using Packaged JAR

```powershell
mvn clean package
java -jar target/student-sphere-0.0.1-SNAPSHOT.jar --spring.profiles.active=mysql
```

### Using Environment Variable (PowerShell)

```powershell
$env:SPRING_PROFILES_ACTIVE = 'mysql'
mvn spring-boot:run
```

---

## 3️⃣ Verify MySQL Connection

Check application logs for:

- Hikari datasource logs
- Hibernate DDL logs

Connect to MySQL using:

| Property | Value |
|----------|-------|
| Host | localhost |
| Port | 3306 |
| Database | studentsphere |
| User | studentsphere_user |
| Password | studentsphere_pass |

---

## 4️⃣ Seed Data

The application contains a `DataSeeder` component which automatically inserts sample data such as:

- States
- Cities
- Colleges

on application startup if tables are empty.

This ensures `collegeId=1` exists for testing signup functionality.

---

## 🗄️ Database Features

- CRUD Operations using Spring Data JPA
- Database Management using MySQL
- ORM Mapping with Hibernate
- Connection Pooling using HikariCP
- Profile-based database configuration

---

## ▶️ How to Run the Project

### Clone Repository

```bash
git clone https://github.com/18anki/student-sphere-java.git
```

### Navigate to Project Folder

```bash
cd student-sphere-java
```

### Install Dependencies

```bash
mvn clean install
```

### Run Application

```bash
mvn spring-boot:run
```

---

## 📸 Screenshots

(Add screenshots of your project here)

Suggested screenshots:
- Dashboard
- Student Management Page
- Attendance Module
- Swagger API Documentation
- Database Tables

---

## 🔮 Future Enhancements

- Spring Security Integration
- JWT Authentication
- Role-based Authorization
- Swagger/OpenAPI Documentation
- Docker Deployment
- CI/CD Pipeline
- Cloud Deployment (AWS)

---

## 👩‍💻 Author

### Ankita Yadav

- GitHub: https://github.com/18anki
- LinkedIn: https://www.linkedin.com/in/ankita-yadav-901127244
