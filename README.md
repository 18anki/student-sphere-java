# StudentsSphere — Local MySQL setup

This project defaults to H2 for development. To run the application against MySQL locally, follow these steps.

Prerequisites
- Docker (for local MySQL) or an existing MySQL server
- Java 17
- Maven

1) Start MySQL with Docker Compose
From the project root run:

```powershell
# start MySQL container in background
docker-compose up -d
```

This uses the provided `docker-compose.yml` which creates a MySQL 8.0 container with:
- DB: studentsphere
- User: studentsphere_user
- Password: studentsphere_pass
- Root password: rootpass

2) Run the Spring Boot app with the `mysql` profile
You can start the app and activate the `mysql` profile in a few ways.

Using Maven:

```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Or using the packaged jar:

```powershell
mvn clean package
java -jar target/student-sphere-0.0.1-SNAPSHOT.jar --spring.profiles.active=mysql
```

Or set the environment variable (PowerShell):

```powershell
$env:SPRING_PROFILES_ACTIVE = 'mysql'
mvn spring-boot:run
```

3) Verify the app connected to MySQL
- Check application logs for Hikari / datasource messages and Hibernate DDL messages.
- Connect to MySQL (e.g. with MySQL Workbench or CLI) using:
  - Host: localhost
  - Port: 3306
  - DB: studentsphere
  - User: studentsphere_user
  - Password: studentsphere_pass

4) Seed data
The app contains `DataSeeder` which seeds sample states, cities and colleges on startup if the tables are empty. That will ensure `collegeId=1` exists for testing signup.

Notes
- For production, switch to Flyway and set `spring.jpa.hibernate.ddl-auto=validate`.
- Keep DB credentials in environment variables or a secrets manager in production.

If you want, I can:
- Add Flyway and an initial migration SQL file, or
- Switch the password hashing to BCrypt via Spring Security and add JWT auth.

