# 
 Retail Product Catalog

A full-stack retail product catalog application built with Spring Boot, Angular, MySQL, JWT Authentication, and Docker.

##  Tech Stack

**Backend**
- Java 17 + Spring Boot 3.2.5
- Spring Security + JWT Authentication
- Spring Data JPA + MySQL
- Maven

**Frontend**
- Angular 21 (Standalone Components)
- Bootstrap 5
- TypeScript

**DevOps**
- Docker + Docker Compose

##  Features

- Full CRUD — Create, Read, Update, Delete products
- JWT Authentication with role-based access control (ADMIN/USER)
- Search by keyword and filter by category
- Pagination
- Backend validation with structured error responses
- 23 unit tests (JUnit 5 + Mockito)
- Fully Dockerized — runs with a single command

##  Authentication

| Action | Public | Admin |
|--------|--------|-------|
| View products | ✅ | ✅ |
| Add product | ❌ | ✅ |
| Edit product | ❌ | ✅ |
| Delete product | ❌ | ✅ |

Default admin credentials:
- Email: `admin@catalog.com`
- Password: `password`

##  Run with Docker
```bash
docker compose up --build
```

- Frontend: http://localhost:4200
- Backend: http://localhost:8080

##  Run Locally

**Backend:**
```bash
cd product-catalog/product-catalog
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd product-catalog-frontend
ng serve
```

##  Run Tests
```bash
cd product-catalog/product-catalog
./mvnw test
```