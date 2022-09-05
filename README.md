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

## Using the API

I have implemented OpenAPI 3.0 Support, as well as Postman Collection and Swagger documentations.

Actuators (service status): http://localhost:8080/actuator

Open API Documentation:
- http://localhost:8080/docs/openapi
- http://localhost:8080/docs/openapi.yaml

Swagger Documentation:
- http://localhost:8080/docs/swagger/index.html

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

