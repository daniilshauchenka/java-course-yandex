# Blog backend (Spring)
Backend of a blog web application implemented using Spring Boot

# Tech stack:
* Java 21
* Spring Boot 3.2
* Spring Web (REST)
* Spring JDBC (no ORM)
* H2  
* Liquibase
* JUnit 5 + Mockito 
* MockMvc
* Maven
* Embedded Tomcat

## Build
```bash
mvn clean package
```

## Tests
```bash 
mvn test
```

## Run Application (Tomcat)
### via Maven:
```bash
mvn spring-boot:run
```
### manually:
```bash
java -jar target/spring-boot-blog-0.0.1-SNAPSHOT.jar
```

### Application will be available at:
```
http://localhost:8080
```