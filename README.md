# Ecommerce

## Technologies
* Java 17
* Spring Boot 3.1.3
* Maven
* H2 (DataBase)

## Dependicies  
* Lombok
* Spring Test
* Junit5
* Mockito
* Jpa

## Librarys   
* Slf4j (Logs)

## Explanations

H2 (Database): H2 is an in-memory and embedded relational database often used for development and testing purposes. It allows you to mimic database interactions without the need for an external database server.

Lombok: A library that reduces boilerplate code by automatically generating getter, setter, and other methods at compile time. It enhances code readability and maintainability.

Spring Test: Part of the Spring Testing framework, this dependency provides support for writing unit and integration tests for Spring components.

JUnit 5: A widely-used testing framework for Java. It's used for writing and executing unit tests to ensure that individual parts of your code work as expected.

Mockito: A mocking framework used in unit testing to create mock objects, allowing you to isolate and test specific parts of your code.

JPA (Java Persistence API): A Java specification for managing relational data in Java applications. It provides an abstraction layer over databases and simplifies database interactions.

Slf4j (Simple Logging Facade for Java): It allows you to switch between different logging implementations without changing your code.

## Configuration

1. Clone this repository to your local machine: `git clone hgit@github.com:nicoreinaldo/Ecommerce.git`
2. Navigate to the project directory: `cd Ecommerce`

## Build and Execution

Make sure you have maven installed - https://maven.apache.org/install.html

`mvn clean install` if not working, try  `./mvnw clean install`
`mvn spring-boot:run` if not working, try `./mvnw spring-boot:run`


## Apis

Once the project is running, we can test our api in various ways, I am going to provide three solutions:

The first is through the console (terminal or cmd) executing a curl to the apis, the second is through postaman (my favorite) and the third directly on the url of the browser.

I will leave the report of the sample api and curl

If you wanna see the tables in database, insert in new tab browser http://localhost:8080/h2-console

Driver Class   : org.h2.Driver

Jdbc url       : jdbc:h2:mem:testdb 

User Name      : sa

And press conect

# API: Carts

## Create a Cart
Create a new cart in the system.

- **Method:** POST
- **URL:** http://localhost:8080/carts

**Responses:**
- 201 Created: Cart created successfully.
  - Response Body: Details of the created cart.
- 500 Internal Server Error: Internal server error occurred while attempting to create the cart.

## Get Cart Information
Get detailed information about a specific cart.

- **Method:** GET
- **URL:** http://localhost:8080/carts/{cartId}

**URL Parameters:**
- {cartId} (UUID, required): Unique identifier of the cart.

**Responses:**
- 200 OK: Cart information retrieved successfully.
  - Response Body: Cart details.
- 400 Bad Request: No cart found with the provided ID.
  - Response Body: Error message.

## Add Products to Cart
Add one or multiple products to an existing cart.

- **Method:** POST
- **URL:** http://localhost:8080/carts/{cartId}/products
- **Headers:** Content-Type: application/json

**URL Parameters:**
- {cartId} (UUID, required): Unique identifier of the cart to which products will be added.

**Request Body:**
```json
[
  {
    "description": "Shoes",
    "amount": 100.0
  },
  {
    "description": "Meet",
    "amount": 20.0
  }
]
```

* description (string, required): Product description.
* amount (number, required): Product amount.

**Responses:**
- 200 OK: Products added to the cart successfully.
- 500 Internal Server Error: Internal server error occurred while attempting to add the products.


# Delete a Cart

Delete a specific cart along with all its contents.

- **Method:** DELETE
- **URL:** `http://localhost:8080/carts/{cartId}`

**URL Parameters:**
- `{cartId}` (UUID, required): Unique identifier of the cart to delete.

**Responses:**
- 200 OK: Cart deleted successfully.
- 204 No Content: Cart could not be deleted (possibly already doesn't exist).
- 500 Internal Server Error: Internal server error occurred while attempting to delete the cart.

## Run Test

`mvn test` if not working, try  `./mvn test`
  


