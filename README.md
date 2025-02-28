## 📚 Book Search Feature

### 📝 Overview

This feature adds the ability to search for books in the catalog using various parameters. Users can perform a search to
find books they may be interested in purchasing.

### 🌐 API Endpoint

The search functionality is exposed via the following endpoint:

```
GET /api/books/search
```

### 🔍 Example Usage

Clients can send a GET request with search parameters to filter books accordingly.

#### Sample Controller Implementation

```java

@GetMapping("/search")
public List<BookDto> searchBooks(BookSearchParametersDto searchParameters) {
    // Implementation logic here
}
```

### 📥 Request Parameters

The request should contain a set of search fields defined in `BookSearchParametersDto`. You can customize the request
fields based on your needs.

#### Example DTO Implementations

**🟢 Option 1: Detailed Search**

```java
public record BookSearchParametersDto(String title, String author, String isbn) {
    // Additional search parameters can be added as required
}
```

**🔵 Option 2: Simplified Search**

```java
public record BookSearchParameters(String titlePart, String author) {
    // Searching over arrays (e.g., multiple authors) can be complex, so this keeps it simpler
}
```

### 🚀 Steps to Implement

1. ✍️ Create the `BookSearchParametersDto` class to define search parameters.
2. 🔧 Implement the `searchBooks` method in the appropriate controller.
3. 🗄️ Ensure the method retrieves books from the database based on provided parameters.
4. ✅ Write unit tests to verify the search functionality.

### 📤 Submitting Your Work

- 🔀 Create a Pull Request (PR) to your course project repository with the implemented changes.
- 🔗 Share the PR link as your homework solution.

### 📌 Additional Notes

If you need to open multiple PRs in parallel and are waiting for previous reviews, please refer to the course
documentation on handling multiple PRs.

---
💡 *For any questions, feel free to ask!*

