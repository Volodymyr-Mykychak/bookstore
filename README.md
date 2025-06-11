# 🔐 Security Homework #2: Spring Security Integration

## 🎯 Goal

Enhance your project by integrating Spring Security:

* Support **Basic Authentication**
* Restrict access to orders by the authenticated user
* Implement **Role-Based Access Control (RBAC)** using `@PreAuthorize`
* Ensure Swagger works properly after securing endpoints

⚠️ You **do not** need to implement the `/login` endpoint!
http://localhost:8080/api/swagger-ui/index.html
---

## ✅ Requirements

### 🔧 Dependencies & Config

* Add `spring-boot-starter-security` to your project
* Create a `SecurityConfig` class with:
    * `PasswordEncoder` bean (e.g. `BCryptPasswordEncoder`)
    * `SecurityFilterChain` bean with public access to:
        * `/api/auth/registration`
        * `/swagger-ui/**`
        * `/v3/api-docs/**`

### 👤 UserDetails Integration

* Implement `UserDetails` in the `User` class
* Implement `UserDetailsService` to load user by email
* Keep `/api/auth/registration` publicly accessible

### 🔡 Role Entity

* Add a `Role` entity with fields:
    * `id` (Long, primary key)
    * `name` (Enum `RoleName`, unique, not null)
* Annotate `Role.name` with `@Enumerated(EnumType.STRING)`
* Add `RoleRepository`
* Update `User` entity:
    * Add `Set<Role> roles` field

### 🗂 Liquibase

* Add changelogs to:
    * Create roles in the DB (e.g. `USER`, `ADMIN`)
    * Assign roles to all existing users
    * Ensure at least one user has the `ADMIN` role
    * Insert only hashed passwords
* Ensure correct execution order (e.g., create `roles` table before `users_roles`)
* `users_roles` table should have a composite key

---

## 🧱 Domain Models

| Entity   | Description                                |
|----------|--------------------------------------------|
| **Book** | Represents books in the store              |
| **User** | Registered user with auth and profile info |
| **Role** | User roles like `ADMIN`, `USER`            |

---

## 👥 Use Cases and Endpoint Access

| Method | Endpoint                                    | Access | Description          |
|--------|---------------------------------------------|--------|----------------------|
| POST   | http://localhost:8080/api/auth/registration | Public | Register a new user  |
| GET    | http://localhost:8080/api/books             | USER   | Get list of books    |
| GET    | http://localhost:8080/api/books/{id}        | USER   | View book details    |
| GET    | http://localhost:8080/api/books/search      | USER   | (Optional) search    |
| POST   | http://localhost:8080/api/books             | ADMIN  | Add a new book       |
| PUT    | http://localhost:8080/api/books/{id}        | ADMIN  | Update existing book |
| DELETE | http://localhost:8080/api/books/{id}        | ADMIN  | Delete a book        |

---

## 📄 Example Request Payloads

### 🧾 Register User

```http
POST http://localhost:8080/api/auth/registration
Content-Type: application/json
```

```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "repeatPassword": "securePassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

---

### 📘 Create Book (ADMIN only)

```http
POST http://localhost:8080/api/books
Authorization: Basic (admin credentials)
Content-Type: application/json
```

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "price": 39.99
}
```

---

### ✏️ Update Book (ADMIN only)

```http
PUT http://localhost:8080/api/books/1
Authorization: Basic (admin credentials)
Content-Type: application/json
```

```json
{
  "title": "Clean Code (2nd Edition)",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "price": 42.50
}
```

---

### ❌ Delete Book (ADMIN only)

```http
DELETE http://localhost:8080/api/books/1
Authorization: Basic (admin credentials)
```

---

## 🔐 Security Test Checklist

### ✅ Public (No Auth Required)

- **POST** http://localhost:8080/api/auth/registration
- **Swagger** http://localhost:8080/swagger-ui/index.htmlhttp://localhost:8080/swagger-ui/index.html

---

### 👤 USER Role (user@example.com / user1234)

- **GET** http://localhost:8080/api/books
- **GET** http://localhost:8080/api/books/1
- **GET** http://localhost:8080/api/books/search?title=Java
- ❌ **POST** http://localhost:8080/api/boks → 403 Forbidden

---

### 👑 ADMIN Role (admin@example.com / admin123)

- **POST** http://localhost:8080/api/books
- **PUT** http://localhost:8080/api/books/1
- **DELETE** http://localhost:8080/api/books/1

---

## ⚠️ Common Mistakes to Avoid

- `is_deleted`: use `valueNumeric: 0` in YAML, not `'false'`
- All users must have a role
- Passwords in Liquibase must be hashed
- Swagger should be publicly accessible
- Use `@PreAuthorize("hasRole('ADMIN')")` where needed
- Don’t hardcode role/user IDs — use `valueComputed`