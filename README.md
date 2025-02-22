# 📚 Spring Boot Data JPA Homework

## 📌 Requirements
This homework focuses on improving the existing `BookRepository` and `BookService` by integrating JPA, Liquibase, and implementing a soft delete mechanism.

### ✅ Task List:
- 🔄 Replace the existing `BookRepository` with one that extends `JpaRepository`.
- 📜 Add **Liquibase** support for database versioning.
- ⚙️ Change the `spring.jpa.hibernate.ddl-auto` property value to `validate`.
- 🗑️ Implement **soft delete** for the `Book` entity.
- 🚀 Add missing endpoints and methods in `BookService`.

## 🌐 API Endpoints
Your `BookController` should expose the following endpoints:

### 📖 **Book Endpoints**:

#### 📚 Retrieve book catalog
- **GET** `/api/books`
- _(Implemented in the previous PR)_

#### 📘 Retrieve book details
- **GET** `/api/books/{id}`
- _(Implemented in the previous PR)_

#### ✍️ Create a new book
- **POST** `/api/books`
- _(Implemented in the previous PR)_

#### ✏️ Update a specific book
- **PUT** `/api/books/{id}`
- **Example request body:**

```json
{
  "title": "Updated Title",
  "author": "Updated Author",
  "isbn": "978-1234567890",
  "price": 19.99,
  "description": "Updated description",
  "coverImage": "https://example.com/updated-cover-image.jpg"
}
```

#### ❌ Delete a specific book (Soft Delete)
- **DELETE** `/api/books/{id}`

## 🗑️ Soft Delete Implementation
- Introduce a new field `deleted` (`boolean`) in the `Book` entity.
- Modify queries to exclude records where `deleted = true`.
- Ensure that the `DELETE` endpoint only marks the book as deleted instead of removing it permanently.

## 🛠️ Database Migration with Liquibase
- Create a `db/changelog/db.changelog-master.xml` file.
- Define table structure updates in changelog XML files.
- Ensure database consistency using Liquibase migration scripts.

## 📤 Submission
- 🔗 Create a **Pull Request (PR)** to your existing course project repository.
- 📎 Share the link to the PR as your HW solution.

> **Note:** If you're waiting for a previous PR review but need to open new PRs in parallel, refer to the provided documentation. 🚀

