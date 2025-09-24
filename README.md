# Getting Started 

## NTT Data - Java Developer Test NTT Data

### Requirements
* Docker
* JDK 21
* Maven 3.8+
* (Optional) GraalVM 22.3+ if you want to build a native executable

### How to run this project

```
$ cd .local
$ docker compose up -d
$ mvn spring-boot:run
```

### What will be running?

* Spring Boot application running on port 8081
* Two instances of MySQL database (Read and Write)
* Redis instance

### Documentation

The project documentation is available at: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

### Default credentials

* User: admin
* Password: admin
* Role: ROLE_ADMIN, ROLE_USER

* User: user
* Password: user
* Role: ROLE_USER

#Database schema

Click [here](https://drawsql.app/teams/car/diagrams/bank) to see the database schema.
