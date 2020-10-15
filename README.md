# TERM-PROJECT: KennUWare

An ERP system developed in Java 11 

##  Component TEAM:  
Manufacturing


## Team

- Noah Kalinowski (Dev Ops)

- Nicholas Pawlak (Dev Ops)

- Kobe Oley (Design & Req Coordinator)

- Kyle McCoy (Team Coordinator)

- Matthew Correa (Integration-QA/Test and Compliance Coordinator)


##Prerequisites

- JDK 11

- IntelliJ - as developer platform (Ultimate preferred)

- MySQL


##How to begin (setup instructions - must keep up to date as project grows in complexity!)
1. Clone the project  

2. In IntelliJ, if open up the **root** of the cloned project. If the
pom files are not imported automatically, right click each `pom.xml` file and
add as a maven project. Ensure there are 2 modules under the root folder, 
"application" and "service".

3. Create the database used by the "service" module 
```
mysql> create database manufacturing; -- Creates the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on manufacturing.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
```
If necessary, change the connection url in `application.propeties`

1. Download the latest relase

4. `chmod +x start`
if you get an error about invalid shell, try `dos2unix start`

5. `./start`

6. Connect to localhost:8081/

7. Sign in using "admin" for the username and password.

##Information
To view generated OpenAPI 3.0 documentation, visit `localhost:8080/manufacturing/api/swagger/ui` 

##License
MIT License

See LICENSE for details.
