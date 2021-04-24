# WebTemplate
Used by me to get started faster on new web projects
Basic web template with backend and frontend

## Frontend
Consists of a Typescript React template
starts up on port 3000
install node.js
`npm start`

## Backend
Backend runs a Spring Boot web server on port 8080

## Techniques
Maven
Websockets without Stomp with methods for connection, disconnection, receiving- and delivering messages 
Rest-service examples using a GET and a POST message

## Google findings

### websockets
Trying example from
https://www.javainuse.com/spring/boot-websocket
Using Websockets without stomp

### logging
https://www.baeldung.com/spring-boot-logging

### databases
https://betterprogramming.pub/building-a-spring-boot-rest-api-part-iii-integrating-mysql-database-and-jpa-81391404046a
Save username and passwords as evnironment variables in ~/.profile
`export DB_USER="root"`
`export DB_PASS="password"`


### Sessions ###
To handle sessions correctly.
The javascript XMLHttpRequest needs to add credentials to be able to request with cookies.
The SESSION-cookie always seem to be set
In the RestController
HttpServletRequest.getCookies()
HttpServletResponse.addCookie()

### Publish ###

set correct server-ip in frontend/src/services/Properties.ts

Build frontend
>/WebTemplate/frontend$ npm run build 

copy files in frontend/build/* to /backend/src/main/resources/static/*

java -jar backend-0.0.1-SNAPSHOT.jar password