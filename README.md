
# Project Introduction

This project is a property management system that allows users to create, view and manage listings. The project was developed using Java 17, Spring Boot 3.2.2 and Spring Security 6.2.

## Framework and Tools

## Framework:

- Spring Boot Starter Parent - Version: 3.2.2
- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Spring Boot Starter Test
- Spring Boot Starter Security
- Spring Boot Starter Mail
- Spring Boot Starter Thymeleaf
- Spring Boot Starter Data Redis
- Spring Boot Starter Cache


## Tools
- MapStruct - Version: 1.5.5.5.Final
- JSON Web Token (JWT) - Version: 0.11.5
- Freemarker - Version: 2.3.29
- JAXB API - Version: 2.3.1
- JAXB Runtime - Version: 3.0.0
- Springdoc OpenAPI - Version: 2.0.3
- Apache POI - Version: 5.2.2
- Jackson Datatype JSR310 - Version: 2.15.4
- Gson - Version: 2.9.1
- Lombok - Version: 1.18.24
- Spring Boot Devtools
- PostgreSQL -Version: 15
- Lombok Mapstruct Binding - Version: 0.2.0
- Mockito Core
- Twilio - Version: 10.1.0


## Installation and Operation

#### Requirements

- Java 17
- Maven 3.6+
- PostgreSQL 15
- Redis

### Steps to set up the project

1. **Clone the warehouse:**
   ```bash
   git clone https://github.com/username/realestate.git
   cd realestate

2. **Update the database configuration:**
-     src/main/resources/application.properties
- file, update the database connection information according to your PostgreSQL installation:

         spring.datasource.url=jdbc:postgresql://localhost:5432/realestate_management_db
         spring.datasource.username=your_db_username
         spring.datasource.password=your_db_password

3. **Update the Redis configuration in the properties file:**
-
         spring.data.redis.host=localhost
         spring.data.redis.port=6379
         spring.data.redis.password=your_redis_password

4. **Compile and run the project:**
-      mvn clean install
-      mvn spring-boot:run

### Project Structure
     -src/main/java: Application source code.
     -src/main/resources: Application configuration files.
     -src/main/resources/application.properties: Configuration file for the application.
### Important Configurations
- POM.xml: Contains the Maven project object model file and dependencies.
# realestate-warmyhomesbackend
