# Personnel accounting
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Bootstrap](https://img.shields.io/badge/bootstrap-%23563D7C.svg?style=for-the-badge&logo=bootstrap&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)

 This project is designed to account for IT company personnel.<br>
 The project provides for the accounting of tasks and tracking the time spent on the tasks of each employee.

## Getting Started
This application is packaged in a Docker container with all its dependencies. Before launching projects, you must build an application. To start the project, run the following command:
```
docker-compose up --build
```
The authorization page will be available at http://localhost:8080/login

## Backend technologies
1. Java
2. Lombok
3. SLF4J-Log4j
4. JUnit
5. MySQL
6. Hibernate
7. HikariCP
8. Flyway
9. Spring
   1. Spring Core 
   2. Spring MVC
   3. Spring Security
   4. Spring REST
   5. Spring AOP
10. Thymeleaf

## Frontend technologies
1. HTML
2. CSS
3. Bootstrap
4. JS
5. jQuery (+DataTables)
6. Ajax

## Database schema
<img src="image/DB.png" alt="DB_schema" style="width:700px; display:block; margin: 10px auto;"/>

## Additional information

Storing profile pictures of employees is done through a Google Drive service account.
The Google Drive API allows you to create apps that leverage Google Drive cloud storage.
This diagram shows the relationship between your Google Drive app, Google Drive, and Google Drive API:
<img src="image/drive-intro.png" alt="Google Drive" style="width:300px; display:block; margin: 10px auto;"/>

 This application uses Token-Based Authentication. You can authorize through the standard authorization form or through the Google Accounts service. </br>
  Token-Based Authentication uses a server-signed token (bearer token), which the client passes to the server in the HTTP Authorization header with the Bearer keyword or in the request body. </br></br>
  An example of the authorization page is presented below:
<img src="image/auth.png" alt="auth page" style="width:300px; display:block; margin: 10px auto;"/>

There are 5 types of users in this system:
1. **Super admin**</br></br>
   1. Employee management (including administrators)
      1. Activating employee accounts
      2. Dismissal of employees
      3. Changing personal data of employees
      4. Changing the employee's role in the system</br></br>
2. **Admin**</br></br>
   1. Employee management
      1. Activating employee accounts
      2. Dismissal of employees
      3. Changing personal data of employees
      4. Changing the employee's role in the system
   2. Department management
      1. CRUD operations
      2. Assigning employees to a department
   3. Managing positions in the system
      1. CRUD operations
   4. Project management
      1. CRUD operations
      2. Assigning employees to projects
      3. Assigning projects to the department
   5. Task Management
      1. CRUD Operations
      2. Tracking the time worked</br></br>
3. **Department head** (all operations are performed within their own department)</br></br>
   3. Project management
      1. CRUD operations
      2. Assigning employees to projects
   4. Task Management
      1. CRUD Operations
      2. Tracking the time worked</br></br>
5. **Project manager** (all operations are performed within their own project)</br></br>
   4. Task Management
      1. CRUD Operations
      2. Tracking the time worked</br></br>
6. **Employee**</br></br>
   1. Tracking the time worked
   
 An example of the main admin menu is presented below:
<img src="image/main.png" alt="main page" style="width:700px; display:block; margin: 10px auto;"/>

 Each user has the ability to change the application interface, view the information of 
 company employees and edit personal information (full name, address, phone number, education, personal skills).

 An example of the telephone directory page is presented below:
<img src="image/Phone_directory.png" alt="phone directory page" style="width:700px; display:block; margin: 10px auto;"/>