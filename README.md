# Spring Boot User Management and Authentication Service
## Table of contents 
1. [General Info](#general-info)
2. [Prerequisites](#prerequisites)
3. [Dependencies](#dependencies)
4. [Project Setup](#project-setup)
5. [Build and Run](#build-and-run)
6. [Run tests and generate jacoco report for code coverage](#run-tests-and-generate-jacoco-report-for-code-coverage)
7. [Formatting code](#formatting-code)
8. [UML diagrams](#uml-diagrams)

### General Info
***
This project is a Spring Boot-based RESTful service that provides functionalities for user account management and authentication using JWT. It consists of 2 end-points which are described below.
## Prerequisites
***
Ensure you have the following installed before running the application:

- Java (version 11)
- Gradle (version 7)
## Dependencies 
***
In the build.gradle file, you can find all the dependencies required to compile and run the project, which are listed below:
- H2 database (configured in `application.properties`)
- SpringBoot (version 2.5.14)
- Jacoco 
- Lombok
- JWT (version 0.12.3)
- Spring security OAuth2 (version 2.5.2)

## Project Setup
***
1. Clone the repository: `git clone https://github.com/fgonzalezj/bci.git`
2. Navigate to the project directory: `cd bci`
3. Configure the database properties in the `application.properties` file.

## Build and Run
***
To build and run the application locally you can execute following commands:
```
./gradlew clean build
./gradlew bootRun
```

By default, the application will be available at http::localhost:8080 with the following end-points:  
- Create a new user, example in curl:
```
curl --location --request POST 'localhost:8080/sign-up' \
  --header 'Content-Type: application/json' \ 
  --data-raw '{
  "name":"",
  "email":"",
  "password":"",
  "phones": [{
  "number":"",
  "city_code":"",
  "country_code":""
  }
  ],
  "roles": [{
  "name":""
  }
  ]
  }'
```  
- Login by token, example in curl:
```
curl --location --request GET 'localhost:8080/login?token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqbGVubm9uamxlbm5vbkBnbWFpbC5jb21wd2QxMjM0NSIsImV4cCI6MTgwOTQ1OTk5Mn0.67D3fFitva2xEL_hqssZXYTq9JjtZLX76gUUpnnFPeo' \
```
Noticed: Replace the token with the token obtained when creating a new user.  
## Run tests and generate jacoco report for code coverage.
***
In order to execute unit tests locally and generate a report for code coverage execute following command:
```
./gradlew test jacocoTestReport
```
Once it finishes, you will be able to see the code coverage report in the directory 'htmlReport/reports/jacoco/index.html'. 
## Formatting code
***
To provide a standard format to our code automatically, we can use the Spotless tool by executing the command:
```
./gradlew spotlessApply
```
## UML diagrams 
***
Into the `/diagrams` directory you can find the UML diagrams for the application.
## Documentation
***
You can find the Swagger documentation at  http://localhost:8080/swagger-ui/ .