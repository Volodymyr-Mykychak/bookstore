[swagger-ui](http://localhost:8080/api/swagger-ui/index.html)

# 🛡️ User Registration – Spring Security (Part 1)

This project implements **user registration** as part of the first phase of introducing security to an online bookstore
web application.  
The goal is to create the basic infrastructure to handle user registration, including DTOs, mapping, validation, and
exception handling.

---

## 📦 Features

✅ Register new users  
✅ DTO + validation of input fields  
✅ Password match verification  
✅ Global exception handling  
✅ Custom validation annotation `@FieldMatch` support

---

## 🧩 Tech Stack

- Java 17 ☕
- Spring Boot 🌱
- Spring Validation ✅
- Lombok 🦾
- MapStruct 🔁
- JPA + Hibernate 🗃️
- MySQL 🐬
- Maven ⚙️

---

## 🧾 Project Structure

```
📁 dto
 └── user
     ├── UserRegistrationRequestDto.java
     └── UserResponseDto.java

📁 controller
 └── AuthController.java

📁 model
 └── User.java

📁 repository
 └── UserRepository.java

📁 mapper
 └── UserMapper.java

📁 validation
 ├── FieldMatch.java
 └── FieldMatchValidator.java

📁 exception
 ├── RegistrationException.java
 └── CustomGlobalExceptionHandler.java
```

---

## 🧪 REST Endpoints

### 🔹 POST /auth/registration

📥 **Request body**:

```json
{
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "repeatPassword": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "shippingAddress": "123 Main St, City, Country"
}
```

📤 **Response body**:

```json
{
  "id": 1,
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "shippingAddress": "123 Main St, City, Country"
}
```

📛 **Possible errors**:

- 🔁 `400 Bad Request` – validation failed (`@Email`, `@FieldMatch`, `@NotBlank`)
- 📧 `409 Conflict` – user with the same email already exists

---

## 📌 Notes

- 🔐 Login is not implemented yet (planned for future parts)
- 🔍 Passwords are stored in hashed form
- 🧪 Tested manually using Postman

---

## 🤝 Assignment for [Mate Academy](https://mate.academy/)


