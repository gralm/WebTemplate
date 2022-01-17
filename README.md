# WebTemplate
Used by me to get started faster on new web projects
Basic web template with backend and frontend

## Frontend
Consists of a Typescript React template
install node.js
`npm start`

## Backend
Backend runs a Spring Boot web server

## Techniques
Websockets without Stomp with methods for connection, disconnection, receiving- and delivering messages 
Rest-service examples using a GET and a POST message

## Google findings
#### Securing website with SSL for https
https://www.digitalocean.com/community/tutorials/how-to-secure-apache-with-let-s-encrypt-on-ubuntu-20-04

#### Websockets, trying example from, Using Websockets without stomp
https://www.javainuse.com/spring/boot-websocket

#### logging
https://www.baeldung.com/spring-boot-logging

### databases
https://betterprogramming.pub/building-a-spring-boot-rest-api-part-iii-integrating-mysql-database-and-jpa-81391404046a
Save username and passwords as evnironment variables in ~/.profile
`export DB_USER="root"`
`export DB_PASS="password"`


### Socket sessions and cookies ###
The javascript XMLHttpRequest needs to add credentials to be able to request with cookies.
The SESSION-cookie is always set
In the RestController cookies can be manipulated with
HttpServletRequest.getCookies()
HttpServletResponse.addCookie()

## Publish 

set correct server-ip in frontend/src/services/Properties.ts

Build frontend
>/WebTemplate/frontend$ npm run build 
> ( cd frontend ; npm run build )

copy files in frontend/build/* to /backend/src/main/resources/static/*
> cp -TRv frontend/build/ backend/src/main/resources/static/

> (cd backend ; mvn install)

> java -jar backend-0.0.1-SNAPSHOT.jar password

## HTTPS Certificates
Show information in certificate
openssl x509 -in mittCert.pem -text

Create certificate
openssl req -x509 -newkey rsa:4096 -keyout myKey.pem -out mittCert.pem -days 365 -nodes

Create a keystore from your cert:
openssl pkcs12 -export -out keyStore.p12 -inkey myKey.pem -in cert.pem
Make sure the keystore filename does not have any capital letters because spring boot has trouble finding the file :p 
keyStore.p12 should be renamed to keystore.p12




## Certbot kommands:
### Step 1. Create letsencrypt-certificate with certbot
`certbot certonly --apache -d webpage.se -d www.webpage.se`

Command to show certificates on server
`certbot certificates`


### Step 2. Transforming the pem-files to a trust-store for spring boot
https://community.letsencrypt.org/t/how-to-create-p12-certificate-for-plex-media-server-by-combining-cert-pem-and-privkey-pem/144728/2

`openssl pkcs12 -export -out mycert.p12 -inkey /etc/letsencrypt/live/webpage.com/privkey.pem -in /etc/letsencrypt/live/webpage.com/cert.pem -certfile /etc/letsencrypt/live/webpage.com/chain.pem -password pass:anything`


## Eternal CORS Trouble
Q: Cookie “uuid_name” will be soon treated as cross-site cookie against 
“https://localhost:8443/static/js/main.1a498980.chunk.js” because the scheme does not match.

A: But to clarify a little, you don't have to delete ALL of your cookies to resolve this. In Firefox, 
you can delete individual site cookies, which will keep your settings on other sites.

To do so, click the hamburger menu in the top right, then, Options->Privacy & Security or Settings->Privacy & 
Security From here, scroll down about half-way and find Cookies and Site Data. Don't click Clear Data. Instead, 
click Manage Data. Then, search for the site you are having the notices on, highlight it, and Remove Selected

Q: Cross-Origin Request Blocked: The Same Origin Policy disallows reading the remote resource at 
https://localhost:8443/postmessage. (Reason: CORS header ‘Access-Control-Allow-Origin’ missing). Status code: 403.

A: remove spring security or adjust Filter or WebMvcConfigurer   
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

Q: Strict-Transport-Security: The connection to the site is untrustworthy, so the specified header was ignored.

Q: Some cookies are misusing the “SameSite“ attribute, so it won’t work as expected