# Spring Boot User Management and Authentication Service

This project is a Spring Boot-based RESTful service that provides functionalities for user account management and authentication.
## Prerequisites

Ensure you have the following installed before running the application:

- SpringBoot (version 2.5.14)
- Java (version 11)
- Gradle (version 7)
- H2 database (configured in `application.properties`)

## Project Setup

1. Clone the repository: `git clone https://github.com/fgonzalezj/bci.git`
2. Navigate to the project directory: `cd bci`
3. Configure the database properties in the `application.properties` file.

## Build and Run
To build and run the application locally you can execute following commands:
```
./gradlew clean build
./gradlew bootRun
```

By default, the application will be available at http::localhost:8080 with the following endpoins:  
- Create a new user:  
Example in curl:
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
  "name":"ROLE_ADMIN"
  }
  ]
  }'
```  
- Login by token:  
  Example in curl:
```
curl --location --request GET 'localhost:8080/login?token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqbGVubm9uamxlbm5vbkBnbWFpbC5jb21wd2QxMjM0NSIsImV4cCI6MTgwOTQ1OTk5Mn0.67D3fFitva2xEL_hqssZXYTq9JjtZLX76gUUpnnFPeo' \
```
Noticed: Replace the token with the token obtained when creating a new user.  
## Run tests and generate jacoco report for code coverage.
In order to execute unit tests locally and generate a report for code coverage execute following command:
```
./gradlew test jacocoTestReport
```
Once it finishes, you will be able to see the code coverage report in the directory 'build/reports/jacoco/index.html'. 
## Formatting code
To provide a standard format to our code automatically, we can use the Spotless tool by executing the command:
```
./gradlew spotlessApply
```