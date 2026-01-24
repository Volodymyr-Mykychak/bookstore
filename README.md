# 📚 Bookstore Web Service: Testing & Reliability 🧪

This module focuses on enhancing the project's reliability by implementing a comprehensive suite of tests. We covered
the existing functionality for both **Book** and **Category** entities across all architectural layers.

---

## 🚀 Overview

To ensure a robust application, we implemented three types of testing:

1. **Controller Layer Tests**: Verifying API endpoints and security roles using `MockMvc`.
2. **Service Layer Tests**: Validating business logic and DTO mapping using `JUnit 5` and `Mockito`.
3. **Repository Layer Tests**: Testing database interactions and custom queries using `@DataJpaTest` and an `H2`
   in-memory database.

---

## 🛠 Features Tested

### 📖 Book Module

* **Controller**: CRUD operations, pagination, and role-based access control (RBAC).
* **Service**: Logic for saving, updating, and stock management (`updateStock`).
* **Repository**: Custom JPQL/Native queries for quantity updates and category-based searches.

### 🏷 Category Module

* **Controller**: Category management and retrieving books by category.
* **Service**: Category CRUD logic and mapping via `MapStruct`.
* **Repository**: Standard CRUD and relational mapping.

---

## 📈 Code Coverage

The goal was to achieve at least **50% line coverage** for the core classes. Through rigorous testing of both "happy
path" and "edge case" scenarios, the following classes have been thoroughly covered:

* `BookController` & `CategoryController`
* `BookServiceImpl` & `CategoryServiceImpl`

---

## 🧰 Tech Stack

* **JUnit 5** & **AssertJ**: For writing and asserting test cases.
* **Mockito**: For mocking dependencies in the service layer.
* **MockMvc**: For simulating HTTP requests to controllers.
* **H2 Database**: For fast, isolated repository testing.
* **Spring Security Test**: For verifying authentication and authorization.

