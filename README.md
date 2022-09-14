# Tempo Users, Teams and Roles

This is the project basic documentation for the Tempo Orchestrator, composed by an implementation of 3 services:

- Users
- Teams
- Role

## Approach the Problem and Solution

I have implemented the solution of supporting the concept of Role by adding a Role Controller, which uses the Role Service, which then uses the Role Repositories. The main idea was to implement a flexible way to link a User to any Role in a given Team, so that a User can have one Role, but different Role for different Teams, as shown in the following diagram:

<p align="center">
<img width="639" alt="image" src="https://user-images.githubusercontent.com/1322483/189249774-3c4d0351-ae22-4346-938e-a79828f3d4df.png">
</p>

### Main activities

- [x] (Re)implementation of the same Tempo API, but including:
  - Restful API for Users and Teams endpoints, such that allowing CRUD operations based on REST dialect (POST, GET, PATCH and DELETE);
- [x] Usage of Spring Actuator for basic observability;
- [x] Support for OpenAPI 3.1 using Spring Docs, including Swagger and .yaml for import the API/Documentation in Postman;
- [x] Web client implementation (using Feign) for loading current data (users and teams) from the Tempo API. You can set the max quantity for each set of data by providing the property io.tempo.teams.bootstrap.users.quantity or IO_TEMPO_TEAMS_BOOTSTRAP_USERS_QUANTITY env variable. This also applies for the teams); 
- [x] Docker image using OpenJDK 16 with Docker Compose;
- [x] Fields validations and view layer modifications using Spring Boot Validation and Annotations, including the development of our custom validation annotations (examples available in io.tempo.teams.util.validators);  
- [x] Unit tests using JUnit 5 and Gradle (see below);
- [x] API automation tests implemented using Postman;
- [x] Implementation of a bootstrap data mechanism based on yaml (eg. to add pre-defined roles);
- [x] Ability to associate team members with roles for a team.

### Extra points activities implemented

- [x] Look up a role for a membership, where membership is defined by a user id and a
team id (method io.tempo.teams.roles.RolesController.lookupUserRole)
- [x] Look up memberships for a role (method io.tempo.teams.roles.RolesController.lookupMembershipOfRole)
- [x] Create a new role (method io.tempo.teams.roles.RolesController.add)
- [x] Assign a role to a member (method io.tempo.teams.roles.RolesController.setUserRole)
- [ ] (Need more time for that) A React web app written in TypeScript that uses the APIs implemented 

## Application Settings

I have decided to import the current online data to a local database in order to show my skills related to implement web clients using Feign.

For that, I have provided some parameters in the application settings (application.yaml) to set the max number of instances (users and teams) to load from the Tempo online API. Current, the application runs bootstrap components and their settings can be controlled by defining some properties, as following:

- `io.tempo.teams.bootstrap.enabled (boolean, default is false)` - toogle to load bootstrap or not
- `io.tempo.teams.bootstrap.users.bootstrapData (boolean, default is false)` - toogle to load users online data
- `io.tempo.teams.bootstrap.teams.bootstrapData (boolean, default is false)` - toogle to load teams online data
- `io.tempo.teams.bootstrap.users.quantity (integer, default is the max found)` - define the max number of users instance to load from online data
- `io.tempo.teams.bootstrap.teams.quantity (integer, default is the max found)` - define the max number of teams instance to load from online data

Alternatively, you can set env variables instead of change the `application.yaml`. For example, to enable bootstrap you can set the env variable `IO_TEMPO_TEAMS_BOOTSTRAP_ENABLED`.

## Run the application

I have implemented docker/docker-compose to run the application. I have implemented a bash script to run all commands at once or you can execute each step (build, test and run) separately. 

### All in one (test, build and run)

```shell
./build-test-run.sh
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
docker-compose up --build
```

## Logs and Output

As a result of executing the app, you will see some outputs similar to the following:

<img width="1183" alt="image" src="https://user-images.githubusercontent.com/1322483/188510051-03ea2721-6208-4169-808a-2c96ad8c4d16.png">

## Technology Stack

- Kotlin 1.6
- Spring 5.3.2
- Spring Boot 2.7.8
- Spring Docs 1.6
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

Alternatively, you can use the Postman collection I have already imported, which is available at [https://www.getpostman.com/collections/e8b849c6f9d26b6def2c](https://www.getpostman.com/collections/8b94a3131d9c00f4bd0c) and provides automated API tests using Javascript.

### Swagger Documentation:

Swagger documentation is available in the following URL:

- http://localhost:8080/docs/swagger/index.html

<img width="1158" alt="image" src="https://user-images.githubusercontent.com/1322483/188509863-15778c67-82ae-4e0e-b10c-f5ffbfe55f5d.png">

## Observability

I have implemented the basic observability function called Spring Boot Actuator.

### Actuator (service status):

The service status can be checked in the following URL:

http://localhost:8080/actuator

<p align="center">
<img width="676" alt="image" src="https://user-images.githubusercontent.com/1322483/188512039-6b9e4998-c04b-4a23-a405-b6a26ed56fb2.png">
</p>

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
   - The concept of team leader could be changed to Role, where a Role called Team Leader is created. That will can allow (or not) a team has multiple leaders.
   - Support a user to create multiple teams
   - Categorization
   - Get Teams a user is the leader
   - Search teams by attributes
5. Implementation of Projects
6. Thinking about scalability, I would split this application in two different microservices:
   1. users
   2. teams 
7. Better handle some operations exceptions (eg. add a user to a team)
8. Return a better structure for not found requests (eg. getting a user that id is not in the database). Nowadays we are return null.
9. Add more unit tests
10. Implement CI/CD pipilines & Gitflow
11. Implement the API using asynchronous event-driven requests (use WebFlux)
