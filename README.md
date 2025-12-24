Internship Technical Test – December 2025
Candidate: Ahmed L'harime
Email: ahmedlharime@gmail.com


# Hahn Internship - README

## Project Description

Hahn Internship is a full-stack web application designed for **project and task management**.  

It enables users to:
- Create and manage projects
- Add tasks to each project
- Track project progress through the percentage of completed tasks

---

## Tools & Technologies

- **Backend**: Java 17 + Spring Boot + Spring Security + Hibernate/JPA
- **Frontend**: React 18 + Tailwind CSS + Headless UI + Heroicons
- **Database**: MySQL

---

## Database Setup

Spring Boot will automatically create the required tables on startup when the following properties are configured in `application.properties`:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/hahn_internship?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.show-sql=true
The generated tables will include: user, project, task, and their relationships.
```

# Running the Backend
Navigate to the backend directory:
Bashcd backend
Build the project with Maven:
Bashmvn clean install
Start the Spring Boot application:
Bashmvn spring-boot:run
The backend API will be available at http://localhost:8081.

# Running the Frontend
Navigate to the frontend directory:
Bashcd frontend
Install dependencies:
Bashnpm install
Start the React development server:
Bashnpm start
The frontend will be available at http://localhost:3000.

Features

Authentication: User registration and login using JWT
Projects: Create, list, and view project details
Tasks: Add tasks, mark them as completed, and monitor progress
UI: Modern, responsive interface built with Tailwind CSS, including profile menu and modals
