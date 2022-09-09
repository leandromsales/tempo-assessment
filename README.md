# Tempo Users, Teams and Roles

This is the project basic documentation for the Tempo Orchestrator, composed by an implementation of two services:

- Users
- Teams 

## Approach the Problem and Solution

I have implemented the resolution of supporting the concept of Role by adding a Role Controller, which uses the Role Service, which then uses the Role Repositories. The main idea was to implement a flexible way to link a User to any Role in a given Team, so that a User can have one Role, but different Role in different Teams, as shown in the following diagram:

<img width="639" alt="image" src="https://user-images.githubusercontent.com/1322483/189249774-3c4d0351-ae22-4346-938e-a79828f3d4df.png">


### Main activities

- [x] (Re)implementation of the same Tempo API, but including:
  - Restful API for Users and Teams endpoints, such that allowing CRUD operations based on REST dialect (POST, GET, PATCH and DELETE);
- [x] Usage of Spring Actuator for basic observability;
- [x] Support for OpenAPI 3.1 using Spring Docs, including Swagger and .yaml for import the API/Documentation in Postman;
- [x] Web client implementation (using Feign) for loading current data from the online API; 
- [x] Docker image using OpenJDK 16 with Docker Compose;
- [x] Fields validations and view layer modifications using Spring Boot Validation and Annotations, including the development of our custom validation annotations (examples available in io.tempo.teams.util.validators);  
- [x] Unit tests using JUnit 5 and Gradle (see below);
- [x] API automation tests implemented using Postman;- 

### Extra points activities implemented

- [x] Look up a role for a membership, where membership is defined by a user id and a
team id
- [x] Look up memberships for a role (method io.tempo.teams.roles.RolesController.lookupMembershipOfRole)
- [x] Create a new role (method io.tempo.teams.roles.RolesController.add)
- [x] Assign a role to a member ((method io.tempo.teams.roles.RolesController.setUserRole))
- [ ] (Need more time for that) A React web app written in TypeScript that uses the APIs implemented 

## Run the application

I have implemented docker/docker-compose to run the application. I have implemented a bash script to run everything all commands or you can execute each step (build, test and run) separately. 

### All in one (build, test and run)

```shell
./build-and-run.sh
```

### Step 1: Build the app
```shell
gradle build -x test
```

### Step 2: Test the app
```shell
gradle test
```

### Step 3: Build the docker container
```shell
docker build -t tempo/services/orchestrator .
```

### Step 4: Run the app
```shell
docker-compose up
```

## Logs and Output

As a result of executing the app, you will see some outputs similar to the following:

<img width="1183" alt="image" src="https://user-images.githubusercontent.com/1322483/188510051-03ea2721-6208-4169-808a-2c96ad8c4d16.png">

## Technology Stack

- Kotlin 1.6
- Spring Boot 2.7.8
- Docker (including Docker Compose)
- HSQLDB (it could be also any other relational database)

## API Usage

I have not implemented the React app, but all the endpoint calls can be tested via Postman and/or Swagger interface, as explained in the next section.

## Documentation

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

1. Support user authentication
   - Protect API endpoints that must be invoked only by admin users
   - Only allow administrative operations if role is admin, otherwise user will only be able to edit is profile
2. Support for email and push notification in some operations
3. Users operation
   - Forgotten password
   - Upload avatar
   - Limit users quantity with pagination
   - Login history
   - Search user by attributes
   - Validate avatarUrl when POST/PATCH a user
4. Teams
   - Support a user to create multiple teams
   - Categorization
   - Get Teams a user is the leader
   - Search teams by attributes
5. Implementation of Projects
6. Thinking about scalability, I would split this application in two different microservices:
   1. users
   2. teams 

