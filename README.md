# 📚 Online Book Store API: Order Management System

Welcome to the **Order Management** module of the Online Book Store API. This update introduces full checkout
functionality, order history tracking, and administrative order status management.

## 🚀 Key Features

* 🛒 **Checkout Process**: Convert shopping cart items into a finalized order.
* 📜 **Order History**: Users can view their past purchases and specific item details.
* 🛡️ **Admin Controls**: Administrators can manage the order lifecycle (Pending, Shipped, Delivered, etc.).
* 📊 **Deduplicated Data**: Prices are snapshotted at the moment of purchase within `OrderItem`.

---

## 🏗️ Domain Models

### 📦 Order Entity

Represents a completed purchase by a user.

* **Fields**: `id`, `user`, `status`, `total`, `orderDate`, `shippingAddress`, `orderItems`.

### 📑 OrderItem Entity

Represents a specific book within an order.

* **Fields**: `id`, `order`, `book`, `quantity`, `price`.

---

## 🛠️ API Endpoints

### 👤 User Operations (`ROLE_USER`)

| Method   | Endpoint                               | Description                                       |
|----------|----------------------------------------|---------------------------------------------------|
| **POST** | `/api/orders`                          | Place a new order from the current shopping cart. |
| **GET**  | `/api/orders`                          | Retrieve the authenticated user's order history.  |
| **GET**  | `/api/orders/{orderId}/items`          | View all items within a specific order.           |
| **GET**  | `/api/orders/{orderId}/items/{itemId}` | View details of a specific item in an order.      |

### 🔑 Admin Operations (`ROLE_ADMIN`)

| Method    | Endpoint           | Description                                                  |
|-----------|--------------------|--------------------------------------------------------------|
| **PATCH** | `/api/orders/{id}` | Update the status of any order (e.g., COMPLETED, DELIVERED). |

---

## 📝 Request Examples

### Place an Order

**POST** `/api/orders`

```json
{
  "shippingAddress": "123 Spring Street, Java City, 01001"
}

```

### Update Order Status (Admin)

**PATCH** `/api/orders/{id}`

```json
{
  "status": "DELIVERED"
}

```

---

## ⚙️ Technical Requirements & Features

* ✅ **Liquibase**: Database migrations for `orders` and `order_items` tables.
* 🗑️ **Soft Delete**: All entities use a `is_deleted` flag instead of hard deletion.
* ⚡ **Lazy Loading**: `FetchType.LAZY` used on all relational mappings to optimize performance.
* 📖 **Pagination & Sorting**: Implemented for all collection endpoints.
* 🔍 **Swagger UI**: Full API documentation and testing interface.
* 🛡️ **Validation**: Robust DTO validation using `jakarta.validation`.

---

## 🔐 Security Matrix

| Role       | Access Level                                                     |
|------------|------------------------------------------------------------------|
| **Public** | Auth (Login/Register)                                            |
| **USER**   | Browse Books, Categories, Manage Own Cart, Place/View Own Orders |
| **ADMIN**  | Full Book/Category CRUD, Update Any Order Status                 |

---