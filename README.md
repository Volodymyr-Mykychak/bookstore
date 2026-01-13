# 🛒 Shopping Cart Feature — Spring Boot Practice

## 📌 Overview

This task is part of a Spring Boot practice project.
The goal is to implement **Shopping Cart functionality**, allowing users to add books, manage quantities, and view their
cart before placing an order.

After completing this task, your application will support **full shopping cart management** for users with the `USER`
role.

---

## 🎯 User Use Cases (ROLE: USER)

### 🧺 Shopping Cart Management

* **Add a book to the shopping cart**

    * `POST /api/cart`

* **View shopping cart contents**

    * `GET /api/cart`

* **Update quantity of a book in the cart**

    * `PUT /api/cart/items/{cartItemId}`

* **Remove a book from the cart**

    * `DELETE /api/cart/items/{cartItemId}`

---

## 🧱 Domain Models (Entities)

After completing this task, the following entities must exist in the project:

* 📘 **Book**
* 👤 **User**
* 🧑‍💼 **Role**
* 🗂 **Category**
* 🛒 **ShoppingCart**
* 📦 **CartItem**

---

## 🛒 ShoppingCart Entity

Represents a user's shopping cart.

**Fields:**

* `id` — `Long` (Primary Key)
* `user` — `User` (**not null**)
* `cartItems` — `Set<CartItem>`

📌 *You may use soft delete for shopping carts.*

---

## 📦 CartItem Entity

Represents a single item in a shopping cart.

**Fields:**

* `id` — `Long` (Primary Key)
* `shoppingCart` — `ShoppingCart` (**not null**)
* `book` — `Book` (**not null**)
* `quantity` — `int` (**not null**)

📌 *Cart items should be removed permanently (no soft delete needed).*

---

## 🔁 Mapper Hint

It may be helpful to add the following method to `BookMapper`:

```java

@Named("bookFromId")
default Book bookFromId(Long id) {
    // implementation
}
```

---

## ⚙️ General Requirements

✅ Use **Liquibase** for database migrations
✅ Do **NOT** use `FetchType.EAGER`
✅ Add **Pagination & Sorting** to all controllers
✅ Add **Swagger/OpenAPI** documentation
✅ Add **validation** to all input DTOs

---

## ❗ Important Notes

* There must be **exactly one shopping cart per user**
* The shopping cart must be created during **user registration**
* Shopping carts are **never deleted**

---

## 🔗 API Endpoints

### 🛒 Shopping Cart Endpoints

#### 📥 Get Shopping Cart

`GET /api/cart`

**Response example:**

```json
{
  "id": 123,
  "userId": 456,
  "cartItems": [
    {
      "id": 1,
      "bookId": 789,
      "bookTitle": "Sample Book 1",
      "quantity": 2
    },
    {
      "id": 2,
      "bookId": 790,
      "bookTitle": "Sample Book 2",
      "quantity": 1
    }
  ]
}
```

---

#### ➕ Add Book to Cart

`POST /api/cart`

**Request example:**

```json
{
  "bookId": 2,
  "quantity": 5
}
```

---

#### ✏️ Update Book Quantity

`PUT /api/cart/items/{cartItemId}`

**Request example:**

```json
{
  "quantity": 10
}
```

---

#### ❌ Remove Book from Cart

`DELETE /api/cart/items/{cartItemId}`

---

## 🔐 Security Requirements

### 🌍 Public (No Authentication)

* `POST /api/auth/register`
* `POST /api/auth/login`

### 👤 USER Role

* `GET /api/books`
* `GET /api/books/{id}`
* `GET /api/categories`
* `GET /api/categories/{id}`
* `GET /api/categories/{id}/books`
* `GET /api/cart`
* `POST /api/cart`
* `PUT /api/cart/items/{cartItemId}`
* `DELETE /api/cart/items/{cartItemId}`

### 🧑‍💼 ADMIN Role

* `POST /api/books`
* `PUT /api/books/{id}`
* `DELETE /api/books/{id}`
* `POST /api/categories`
* `PUT /api/categories/{id}`
* `DELETE /api/categories/{id}`

---

## 🧠 Debugging Hint

If `bookRepository.findById(id)` returns `Optional.empty()` **even though the book exists in DB**:

👉 Check whether the book has a category
👉 Think about the difference between:

* `JOIN FETCH`
* `LEFT JOIN FETCH`

This often causes unexpected behavior when relationships are not loaded correctly.




