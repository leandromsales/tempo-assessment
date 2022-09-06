# Tempo Users, Teams and Roles

## Run the application

I have implemented docker/docker-compose to run the application.

### All in one (build, test and run)

I have implemented a shell script for build, test and run the application. For this case, just run the following command:

```shell
./build-and-run.sh
```

### For building the app
```shell
gradle build -x test
```

### For running tests
```shell
gradle test
```

### For build the app container
```shell
docker build -t tempo/services/orchestrator .
```

### Run the app
```shell
docker-compose up
```

As a result of this command, you will see some outputs similar to this one:

<img width="1183" alt="image" src="https://user-images.githubusercontent.com/1322483/188510051-03ea2721-6208-4169-808a-2c96ad8c4d16.png">

## Technology Stack

- Kotlin 1.6
- Spring Boot 2.7.8
- Docker (including Docker Compose)
- HSQLDB (it could be also any other relational database)

## API Documentation

I have implemented OpenAPI 3.0 Support, as well as Postman Collection and Swagger documentations.

### Open API Documentation:

The OpenAPI 3.0 documentation is available in the following URL:

- http://localhost:8080/docs/openapi
- http://localhost:8080/docs/openapi.yaml

### Postman Documentation:

You can use openapi.yaml and import the API directly to the Postman, resulting on this:

<img width="1130" alt="image" src="https://user-images.githubusercontent.com/1322483/188510155-26860c45-afc1-4044-8db8-c3d9166eb6c3.png">

### Swagger Documentation:

Swagger documentation is available in the following URL:

- http://localhost:8080/docs/swagger/index.html

<img width="1158" alt="image" src="https://user-images.githubusercontent.com/1322483/188509863-15778c67-82ae-4e0e-b10c-f5ffbfe55f5d.png">

## Observability

I have implemented the basic observability function called Spring Boot Actuator.

### Actuator (service status):

The service status can be checked in the following URL:

http://localhost:8080/actuator

<img width="676" alt="image" src="https://user-images.githubusercontent.com/1322483/188512039-6b9e4998-c04b-4a23-a405-b6a26ed56fb2.png">

## Improvements to be done

1. Support user login
2. Protect API endpoints that must be invoked only by admin users
  2.1. Only allow administrative operations if role is admin, otherwise user will only be able to edit is profile
3. Support for email and push notification in some operations
4. Users operation
  4.1. Forgotten password 
  4.2. Upload avatar
  4.3. Limit users quantity with pagination
  4.4. Login history
  4.5. Search user by attributes
  4.6. Validate avatarUrl when POST/PATCH a user
5. Teams 
  5.1. Support a user to create multiple teams
  5.2. Categorization
  5.3. Get Teams a user is the leader
  5.4. Search teams by attributes
6. Implementation of Projects

