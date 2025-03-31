A comprehensive Library Management System built using Java Spring Boot. This application provides functionality to manage books, students, transactions, and admin operations.

Main Features
Student Registration and Management
Book Management (Add, Update, Delete, Search)
Book Issuance and Return Management
Admin Management
Transaction Management
Redis Caching for Student Data
Spring Security for Authentication and Authorization
Global Exception Handling

Library-Management-System/
├── src/
│   ├── main/
│   │   ├── java/com/example/minor_project/
│   │   │   ├── config/           # Redis and Security configurations
│   │   │   ├── controller/       # API Controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── model/            # Entity Models (Student, Book, Transaction, User ,Author,Admin)
│   │   │   ├── repository/       # JPA Repositories
│   │   │   ├── service/          # Business Logic Services
│   │   │   ├── util/             # Constants and Utility Functions
│   │   ├── resources/
│   │   │   ├── application.properties
├── .gitignore
├── README.md
├── pom.xml 

User Management

**  Implemented User Registration and Login using Spring Security.**
Support for multiple user roles (Admin and Student).

3.Secure password storage using BCrypt.

Book Management

1.Admins can add, update, delete, and view books.

2.Students can view available books.

Student Management

1.CRUD operations for student records.

2.Students can view their profile and issued books.

Transaction Management

1.Issue and return books .

2.Maintain a record of transactions.


Validation and Error Handling

Used Spring Boot’s @Valid and @Pattern annotations for input validation.

Implemented Global Exception Handling to return meaningful error messages.

Database Management

Utilized MySQL with JPA (Java Persistence API) and Hibernate for database operations.

Established one-to-many and one-to-one relationships for effective data management.

Caching

Integrated Redis for caching frequently accessed data like student and book details.

Security

Secured endpoints using Spring Security with role-based access control.


⚙️ Tech Stack
Backend: Spring Boot, Spring Security
Database: MySQL
Caching: Redis
ORM: Hibernate
Validation: Spring Boot Validation
Logging: SLF4J
API Testing: Postman
Version Control: Git & GitHub