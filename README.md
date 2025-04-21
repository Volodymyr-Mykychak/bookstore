# ✅ Homework: Add Pagination, Sorting, and Swagger to Book Controller

This pull request enhances the `BookController` functionality by adding:

## 🔁 Pagination Support

- Implemented using Spring's `Pageable` and `Page<T>`
- Example usage:
  `GET /books?page=0&size=5`
- Metadata returned includes:
    - Total items
    - Total pages
    - Current page
    - Items per page
    - Is last page

## ↕️ Sorting Support

- Implemented using Spring Data's multi-sort capability
- Example usage:
  `GET /books?sort=price,desc&sort=title,asc`
- Supports chaining by multiple fields

## 📘 Swagger Integration

- All endpoints are now documented with Swagger UI
- Swagger UI available at: `http://localhost:8080/swagger-ui/index.html`

## 🔨 Technical Details

- `BookController` now accepts `Pageable` as a parameter
- `BookService` updated to return `Page<BookDto>`
- `BookRepository` extends `JpaSpecificationExecutor<Book>`
- Swagger enabled using `springdoc-openapi` library

## ✅ Example Request

# GET /books?page=1&size=5&sort=price,desc&sort=title

```json
{
  "content": [
    ...
  ],
  "pageable": {
    "pageNumber": 1,
    "pageSize": 5
  },
  "totalPages": 10,
  "totalElements": 50,
  "last": false,
  "first": false
}
```

🚀 How to Test
Run the application.

Open Swagger UI

Try the /books endpoint with different parameters.

http://localhost:8080/swagger-ui/index.html
http://localhost:8080/api/swagger-ui/index.html