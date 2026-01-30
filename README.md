# 📚 Book store API

## 🌟 Overview

**Bookstore app** is a comprehensive RESTful API designed to power a modern digital library or e-commerce platform.
Built with the **Spring Boot** ecosystem, it provides a seamless experience for browsing books, managing categories, and
handling user orders with security and scalability in mind.

---

## 🏗️ System Architecture

Below is the entity-relationship diagram representing the core data model and connections of the book store app:

```mermaid
erDiagram
    USER ||--o{ USER_ROLE: "has"
    ROLE ||--o{ USER_ROLE: "assigned to"
    USER ||--|| SHOPPING_CART: "owns"
    SHOPPING_CART ||--o{ CART_ITEM: "contains"
    BOOK ||--o{ CART_ITEM: "added to"
    USER ||--o{ ORDER: "places"
    ORDER ||--o{ ORDER_ITEM: "consists of"
    BOOK ||--o{ ORDER_ITEM: "ordered"
    BOOK }|--|{ CATEGORY: "belongs to"

    USER {
        bigint id PK
        string email UK
        string password
        string first_name
        string last_name
        string shipping_address
        boolean is_deleted
    }

    ROLE {
        bigint id PK
        string name UK
    }

    BOOK {
        bigint id PK
        string title
        string author
        string isbn UK
        decimal price
        boolean is_deleted
    }

    CATEGORY {
        bigint id PK
        string name UK
        boolean is_deleted
    }

    SHOPPING_CART {
        bigint id PK
        bigint user_id FK
    }

    CART_ITEM {
        bigint id PK
        bigint shopping_cart_id FK
        bigint book_id FK
        int quantity
    }

    ORDER {
        bigint id PK
        bigint user_id FK
        string status
        decimal total
        datetime order_date
        boolean is_deleted
    }

    ORDER_ITEM {
        bigint id PK
        bigint order_id FK
        bigint book_id FK
        int quantity
        decimal price
    }
```

---

## 🔐 Authentication Flow (Login & JWT)

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Controller as AuthController
    participant Manager as AuthenticationManager
    participant Provider as JwtUtil
    participant DB as MySQL
    User ->> Controller: POST /auth/login (email, password)
    Controller ->> Manager: authenticate(loginRequest)
    Manager ->> DB: SELECT user WHERE email = ?
    DB -->> Manager: User Entity (hashed password)
    Note over Manager: Password check (BCrypt)
    Manager ->> Controller: Authentication Success
    Controller ->> Provider: generateToken(email)
    Provider -->> Controller: JWT Access Token
    Controller -->> User: 200 OK (token)
```

---

## 🔄 Request Flow Diagram (Create Order)

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Security as Security Layer (JWT)
    participant Controller as OrderController
    participant Service as OrderService
    participant Map as OrderMapper
    participant Repo as OrderRepository
    participant DB as MySQL Database
    User ->> Security: POST /api/orders (with JWT)
    Security -->> User: 401 Unauthorized (if token invalid)
    Security ->> Controller: Request Authorized
    Controller ->> Service: createOrder(userId, requestDto)
    Note over Service: Business Logic:<br/>Validate cart, calculate total
    Service ->> Repo: save(orderEntity)
    Repo ->> DB: INSERT INTO orders
    DB -->> Repo: Order ID
    Service ->> Map: toDto(savedOrder)
    Map -->> Service: OrderResponseDto
    Service -->> Controller: OrderResponseDto
    Controller -->> User: 201 Created (OrderResponseDto)
```

---

## 🛒 Add Item to Shopping Cart (Business Validation)

```mermaid
sequenceDiagram
    autonumber
    participant Controller as CartController
    participant Service as ShoppingCartService
    participant Repo as CartItemRepository
    Controller ->> Service: addToCart(userId, requestDto)
    Service ->> Repo: findByCartAndBook(cartId, bookId)

    alt Item exists
        Service ->> Repo: update quantity
    else Item new
        Service ->> Repo: create new CartItem
    end

    Repo -->> Service: saved item
    Service -->> Controller: ShoppingCartDto
```

---

## 🛡️ Role-Based Access Control (Admin vs User)

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Filter as JwtAuthenticationFilter
    participant Proxy as FilterSecurityInterceptor
    participant Controller as BookController
    User ->> Filter: DELETE /api/books/1 (JWT Token)
    Filter ->> Filter: Validate Token & Extract Roles (USER)
    Filter ->> Proxy: Check Access (Requires ROLE_ADMIN)
    Note right of Proxy: Role check failed:<br/>USER != ADMIN
    Proxy -->> User: 403 Forbidden
```

---

## 🔍 Book Search & Filtering Flow

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Controller as BookController
    participant Spec as BookSpecificationBuilder
    participant Repo as BookRepository
    participant DB as MySQL
    User ->> Controller: GET /api/books/search?author=King&price=20
    Controller ->> Spec: build(searchParameters)
    Note over Spec: Dynamically creates<br/>SQL WHERE clause
    Spec -->> Controller: Specification object
    Controller ->> Repo: findAll(specification, pageable)
    Repo ->> DB: SELECT * FROM books WHERE author LIKE %King% AND price <= 20
    DB -->> Repo: List of Books
    Repo -->> Controller: Page of Books
    Controller -->> User: 200 OK (BookResponseDtos)
```

---

## 📺 Project Demo

See the API in action and a brief code walkthrough:
👉 **[Watch the Loom Video Demo](https://www.google.com/search?q=INSERT_YOUR_LOOM_LINK_HERE)**

---

## 🛠 Technology Stack & Tools

The project is built using a modern, industry-standard stack to ensure security, scalability, and ease of maintenance:

| Category            | Tools & Technologies                                                                                                                                          | Purpose                                                   |
|:--------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------|
| **Backend Core**    | ![Java](https://img.shields.io/badge/Java-22-ED8B00?logo=java) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.7-6DB33F?logo=springboot)          | Core framework and runtime environment.                   |
| **Security**        | ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?logo=springsecurity) ![JWT](https://img.shields.io/badge/JWT-black?logo=jsonwebtokens) | Authentication and role-based authorization (RBAC).       |
| **Persistence**     | ![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql) ![H2](https://img.shields.io/badge/H2-Database-blue)                                           | Production and in-memory testing databases.               |
| **DB Migrations**   | ![Liquibase](https://img.shields.io/badge/Liquibase-202221?logo=liquibase)                                                                                    | Version control for database schema changes.              |
| **Mapping & Utils** | ![MapStruct](https://img.shields.io/badge/MapStruct-orange) ![Lombok](https://img.shields.io/badge/Lombok-red)                                                | Reducing boilerplate code for DTO mapping and POJOs.      |
| **API Docs**        | ![OpenAPI](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger)                                                                                          | Interactive API documentation and testing via Swagger UI. |
| **Testing**         | ![Mockito](https://img.shields.io/badge/Mockito-lightgrey) ![Testcontainers](https://img.shields.io/badge/Testcontainers-yellow)                              | Unit and integration testing with isolated environments.  |
| **Deployment**      | ![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker) ![Docker Compose](https://img.shields.io/badge/Docker_Compose-2496ED?logo=docker)           | Containerization and multi-container orchestration.       |

---

## 🚀 Key Features

* **Inventory Management:** Full CRUD for books and categories with advanced filtering.
* **Security:** Role-based access control (RBAC).
* `ADMIN`: Full control over the catalog.
* `USER`: Search books and manage personal shopping cart/orders.


* **Shopping Cart:** Real-time management of selected items.
* **Order System:** Automated checkout process with status tracking.

---

## ⚙️ Installation & Setup

### Required Software

* Java JDK (17 or 22)
* Maven
* Docker & Docker Compose

### Step-by-Step Instructions

1. **Clone the repository:**

```bash
https://github.com/Volodymyr-Mykychak/bookstore.git
cd bookstore

```

2. **Configure Environment:**

```bash
cp .env.template .env
# Edit .env file to match your local configuration (DB credentials, etc.)

```

3. **Build & Run via Docker (Recommended):**

```bash
mvn clean package -DskipTests
docker-compose up --build

```

```bash
docker compose down -v
```

4. **Manual Run (Alternative):**

```bash
mvn spring-boot:run

```

---

## 📖 Usage & API Documentation

Once the application is running, you can explore the endpoints via Swagger UI:
🔗 **http://localhost:8080/api/swagger-ui.html**

**Default Credentials (Admin):**

* **Email:** `admin@example.com`
* **Password:** `12345678`

** [Admin Authentication](http://localhost:8080/api/swagger-ui/index.html#/Authentication/login) **

```json
{
  "email": "admin@example.com",
  "password": "12345678"
}

```

---

### 📝 API Request Examples

#### 1. [User Registration](http://localhost:8080/api/swagger-ui/index.html#/Authentication/registerUser)

Use this payload to create a new account.

```json
{
  "email": "user@example.com",
  "password": "Password123",
  "repeatPassword": "Password123",
  "firstName": "John",
  "lastName": "Doe",
  "shippingAddress": "123 Bookstore Street, New York, NY"
}

```

#### 2. [User Authentication](http://localhost:8080/api/swagger-ui/index.html#/Authentication/login)

Use these credentials to obtain your JWT access token.

```json
{
  "email": "user@example.com",
  "password": "Password123"
}

```

#### 3. [Adding a New Category](http://localhost:8080/api/swagger-ui/index.html#/Categories/createCategory)

```json
{
  "name": "Programming",
  "description": "Books about software development and languages"
}
```

#### 4. [Adding a New Book](http://localhost:8080/api/swagger-ui/index.html#/Books/createBook)

```json
{
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "isbn": "9780134685991",
  "price": 45.00,
  "description": "A comprehensive guide to best practices for the Java platform.",
  "coverImage": "https://example.com/images/effective-java.jpg",
  "quantity": 15,
  "categoryIds": [
    1
  ]
}

```

####  4.1 [Get books](http://localhost:8080/api/swagger-ui/index.html#/Books/getAllBooks)
####  4.2 [Get categories](http://localhost:8080/api/swagger-ui/index.html#/Categories/getAllCategories)
####  4.3 [Get search](http://localhost:8080/api/swagger-ui/index.html#/Books/searchBooks)

#### 5. [Post cart](http://localhost:8080/api/swagger-ui/index.html#/Shopping%20Cart/addBook)

```json
{
  "bookId": 1,
  "quantity": 2
}
```

#### 6. [Post orders](http://localhost:8080/api/swagger-ui/index.html#/order-controller/createOrder)

```json
{
  "shippingAddress": "32 Mykoly Zakrevskoho St, Kyiv, 02222, Ukraine"
}
```


---

## 🧪 Testing

The project ensures reliability through unit and integration tests.
To run the test suite, use:

```bash
mvn test  

```

---

## 💡 Challenges Faced

During the development of this API, I focused on solving several key architectural problems:

* **Data Integrity:** Implementing **Soft Delete** logic using Hibernate annotations to preserve historical data.
* **Performance:** Optimizing database queries for book searching and filtering to ensure fast response times.
* **Security:** Configuring a stateless **JWT-based authentication** flow that integrates seamlessly with Spring
  Security.

---

### 🛠 Troubleshooting & Clean Start

If you encounter issues with the database (e.g., Liquibase checksum errors) or want to reset the application state
completely, follow these steps to ensure a **clean start**:

1. **Stop and remove all containers, networks, and volumes:**

> The `-v` flag is crucial here as it deletes the persistent MySQL data, allowing Liquibase to re-run all migrations on
> a fresh database.

```bash
docker compose down -v

```

2. **Rebuild the application and start the containers:**

```bash
mvn clean package -DskipTests
docker compose up --build

```

---

## 📊 Database Management

Since the application runs in a Docker container, you can access the **MySQL** database directly via terminal to verify
data persistence and user roles.

### Accessing the Database

To log into the MySQL terminal inside the running container, use the following command:

```bash
docker exec -it bookstore-mysqldb-1 mysql -u root -ppassword bookstore

```

*(Note: Replace `password` with the value from your `.env` file if it differs).*

### Useful SQL Queries

Once inside the MySQL shell, you can use these queries to verify the system state:

* **Check Registered Users:**

```sql
SELECT id, email, first_name FROM users;

```

* **Verify User Roles:**

```sql
SELECT u.email, r.name 
FROM users u 
JOIN users_roles ur ON u.id = ur.user_id 
JOIN roles r ON ur.role_id = r.id;

```

* **Promote User to Admin:**

```sql
UPDATE users_roles SET role_id = 1 WHERE user_id = 2;

```

---

## 🤝 Acknowledgements

* **Mate Academy** mentors and students for the technical support.
* The **Spring Community** for the excellent documentation.

---

**Developed by:** [Your Name]
*Feel free to reach out for collaboration or questions!*

---