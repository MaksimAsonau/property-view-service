## Property View Service
Spring Boot REST API for managing hotels.

### Tech stack
- Java 21
- Spring Boot
- Spring Data JPA (Specifications)
- H2 / PostgreSQL / MySQL
- Liquibase
- MapStruct
- Swagger (OpenAPI)

### Run
- H2 (default):

      mvn spring-boot:run

Switch DB profiles (quick switching is implemented via application-h2.yaml / application-postgres.yaml / application-mysql.yaml)
- H2:
               
      mvn spring-boot:run -Dspring-boot.run.profiles=h2
- PostgreSQL:

      mvn spring-boot:run -Dspring-boot.run.profiles=postgres
- MySQL:

      mvn spring-boot:run -Dspring-boot.run.profiles=mysql

### Base URL
http://localhost:8092/property-view

### Swagger
http://localhost:8092/property-view/swagger-ui/index.html

### Tests
    mvn test
