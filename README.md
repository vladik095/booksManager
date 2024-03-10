# Log and QR Code Management

This controller manages logs and QR codes in the application.

## Endpoints

### Log Management

- **GET /api/logs**: Retrieve all logs. Can be filtered by author by passing the `author` parameter.
- **GET /api/logs/{id}**: Retrieve a log by its identifier.
- **POST /api/logs**: Create a new log. Data about the new log should be sent in the request body.
- **PUT /api/logs/{id}**: Update an existing log. Data about the updated log should be sent in the request body.
- **DELETE /api/logs/{id}**: Delete a log by its identifier.
- **DELETE /api/logs**: Delete all logs.
- **GET /api/logs/links**: Retrieve logs that are links.
- **GET /api/log/searchByAuthor**: Search logs by author. Can pass the `author` parameter for searching.

### QR Code Management

- **GET /api/generateQRCode**: Generate a QR code based on the provided text. Accepts a `text` parameter containing the text for generating the QR code.
- **GET /api/qrCodeDescription/{id}**: Generate a QR code based on the description of a log. Accepts an `id` parameter identifying the log, and generates a QR code based on the description.

## Database Description

This application uses a PostgreSQL database. It contains a table named `logs` with the following fields:

- `id`: Unique event identifier (type `long`).
- `Author`: Event author (type `String`).
- `QRdescription`: Description of the event for generating a QR code (type `String`).
- `Link`: Flag indicating whether the event is a link (type `boolean`).

## Dependencies

- **Spring Boot**: For building the application.
- **Spring Web**: For building RESTful APIs.
- **PostgreSQL Driver**: Driver for interacting with the PostgreSQL database.
- **Java Persistence API (JPA)**: For working with object-relational mapping.
- **Apache Maven**: For managing dependencies and building the project.

## How to Run

1. Clone the repository:
    ```
    git clone https://github.com/vladik095/JAVA.git
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

- **Retrieve all logs**:
    ```
    GET /api/logs
    ```

- **Retrieve a log by its identifier**:
    ```
    GET /api/logs/{id}
    ```

- **Create a new log**:
    ```
    POST /api/logs
    ```

- **Update an existing log**:
    ```
    PUT /api/logs/{id}
    ```

- **Delete a log by its identifier**:
    ```
    DELETE /api/logs/{id}
    ```

- **Delete all logs**:
    ```
    DELETE /api/logs
    ```

- **Retrieve logs that are links**:
    ```
    GET /api/logs/links
    ```

- **Search logs by author**:
    ```
    GET /api/log/searchByAuthor?author={author}
    ```

- **Generate a QR code using URL**:
    ```
    GET /api/generateQRCode?text={your_text_here}
    ```

- **Generate a QR code based on log description**:
    ```
    GET /api/qrCodeDescription/{id}
    ```

