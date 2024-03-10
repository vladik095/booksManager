# Book Manager API

This API serves as a book manager, allowing users to perform various operations related to authors, books, and tags within the application. Users can interact with endpoints to retrieve, create, update, and delete books, authors, and tags, as well as manage the relationships between them.

## Endpoints

### Author Management

- **GET /api/authors**: Retrieve all authors.
- **GET /api/authors/{id}**: Retrieve an author by ID.
- **POST /api/authors**: Add a new author.
- **PUT /api/authors/{id}**: Update an existing author.
- **DELETE /api/authors/{id}**: Delete an author.

### Book Management

- **GET /api/books**: Retrieve all books.
- **GET /api/books/{id}**: Retrieve a book by ID.
- **POST /api/books/{authorId}**: Add a new book for a specific author.
- **PUT /api/books/{id}**: Update an existing book.
- **DELETE /api/books/{id}**: Delete a book.
- **POST /api/books/{bookId}/tags/{tagId}**: Add a tag to a book.
- **GET /api/books/{bookId}/tags**: Retrieve tags associated with a book.

### Tag Management

- **GET /api/tags**: Retrieve all tags.
- **GET /api/tags/{id}**: Retrieve a tag by ID.
- **POST /api/tags**: Add a new tag.
- **PUT /api/tags/{id}**: Update an existing tag.
- **DELETE /api/tags/{id}**: Delete a tag.

## Database Description

The application uses a PostgreSQL database with the following relationships:

- **Author-Book Relationship**: One-to-many relationship where one author can have multiple books.
- **Book-Tag Relationship**: Many-to-many relationship where one book can have multiple tags, and one tag can be associated with multiple books.

## Dependencies

- **Spring Boot**: For building the application.
- **Spring Web**: For building RESTful APIs.
- **PostgreSQL Driver**: Driver for interacting with the PostgreSQL database.
- **Java Persistence API (JPA)**: For object-relational mapping.
- **Apache Maven**: For managing dependencies and building the project.

## How to Run

1. Clone the repository:
    ```
    git clone https://github.com/vladik095/booksManager.git
    ```

2. Build the project:
    ```
    mvn clean install
    ```

3. Run the application:
    ```
    mvn spring-boot:run
    ```

4. The application will start, and you can access the API endpoints described above.

## Usage Examples

- **Retrieve all authors**:
    ```
    GET /api/authors
    ```

- **Retrieve a book by its identifier**:
    ```
    GET /api/books/{id}
    ```

- **Add a new tag**:
    ```
    POST /api/tags
    ```

- **Update an existing author**:
    ```
    PUT /api/authors/{id}
    ```

