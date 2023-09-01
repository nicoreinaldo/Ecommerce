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

## Configuration

1. Clone this repository to your local machine: `git clone hgit@github.com:nicoreinaldo/Ecommerce.git`
2. Navigate to the project directory: `cd Ecommerce`

## Build and Execution

Make sure you have Java 17 installed - https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

Make sure you have Maven installed - https://maven.apache.org/install.html

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

<img width="1669" alt="Screenshot 2023-09-01 at 02 33 27" src="https://github.com/nicoreinaldo/Ecommerce/assets/22691843/78ef5485-f8b9-40f3-982f-7add656bd18e">

<img width="1698" alt="Screenshot 2023-09-01 at 02 53 17" src="https://github.com/nicoreinaldo/Ecommerce/assets/22691843/54ed4a10-5442-4e62-9304-3ad8c8ff004d">


  


