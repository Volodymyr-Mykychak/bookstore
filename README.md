# 🧩 Add Category Model

Enhance your Spring Boot application by adding support for a new entity — **Category**.  
This task includes both user and admin functionality, model implementation, and architectural improvements.

---

## 👤 User Use Cases (Role: USER)

Users should be able to browse categories and find books by category.

- `GET /api/categories` — 🔍 Retrieve all categories
- `GET /api/categories/{id}/books` — 📚 Get books by a specific category

---

## 👨‍💼 Admin Use Cases (Role: ADMIN)

Admins should be able to manage categories.

- `POST /api/categories` — ➕ Create a new category
- `PUT /api/categories/{id}` — 🔄 Update category details
- `DELETE /api/categories/{id}` — ❌ Remove category

---

## 🧬 Domain Models

After completing this task, your project should include the following entities:

- `Book` 📘
- `User` 👤
- `Role` 🛡️
- `Category` 🗂️

### 🔧 Category Entity

```java
Long id;
String name;         // required
String description;
````

In `Book` class:

```java
private Set<Category> categories = new HashSet<>();
```

You can use:

```java
List<Book> findAllByCategoryId(Long categoryId);
```

---

## 🗂️ Repositories & Mappers

* Create `CategoryRepository` extending `JpaRepository`

* Add DTOs for `Category`

* Modify `BookMapper`:

    * `BookDto toDto(Book book);`
    * `Book toEntity(CreateBookRequestDto dto);`
    * `BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);`
    * `@AfterMapping setCategoryIds(...)`

* Create `CategoryMapper`:

    * `CategoryDto toDto(Category category);`
    * `Category toEntity(CategoryDto dto);`

---

## 🧠 Services

Create:

* `CategoryService` interface:

    * `List<CategoryDto> findAll();`
    * `CategoryDto getById(Long id);`
    * `CategoryDto save(CategoryDto dto);`
    * `CategoryDto update(Long id, CategoryDto dto);`
    * `void deleteById(Long id);`

* `CategoryServiceImpl` implementation

---

## 🌐 Controllers

Create `CategoryController`:

```java
public CategoryDto createCategory(CategoryDto dto)

public List<CategoryDto> getAll()

public CategoryDto getCategoryById(Long id)

public CategoryDto updateCategory(Long id, CategoryDto dto)

public void deleteCategory(Long id)

public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id)
```

---

## ⚙️ General Requirements

* ✅ Use **Liquibase**
* ✅ Implement **soft delete**
* ✅ Avoid using `FetchType.EAGER`
* ✅ Add **pagination**, **sorting**, **Swagger**
* ✅ Add **validation** to input DTOs
* ✅ Map `List<Long> categoryIds` → `Set<Category>` in `Book`
* ✅ Exclude `categories` from `toString()`, `equals()`, `hashCode()`:

```java

@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(...)
@ToString.Exclude
@EqualsAndHashCode.Exclude
private Set<Category> categories = new HashSet<>();
```

---

## 🔗 Endpoints

### 🧑 USER

* `GET /api/books`
* `GET /api/books/{id}`
* `GET /api/categories`
* `GET /api/categories/{id}`
* `GET /api/categories/{id}/books`

### 🛡️ ADMIN

* `POST /api/books`
* `PUT /api/books/{id}`
* `DELETE /api/books/{id}`
* `POST /api/categories`
* `PUT /api/categories/{id}`
* `DELETE /api/categories/{id}`

### 🔐 Public

* `POST /api/auth/register`
* `POST /api/auth/login`

---

## 🧪 Examples

### ➕ Create Category

**POST /api/categories**

```json
{
  "name": "Fiction",
  "description": "Fiction books"
}
```

### 📋 Get All Categories

**GET /api/categories**

```json
[
  {
    "id": 1,
    "name": "Fiction",
    "description": "Fiction books"
  }
]
```

### 🔄 Update Category

**PUT /api/categories/{id}**

```json
{
  "name": "Fiction",
  "description": "Fiction books"
}
```



