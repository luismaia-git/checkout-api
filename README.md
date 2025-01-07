# Checkout Service API 

### Checkout API: Cart Management, Promotions, and Payments Savings
## Overview
The API is designed to support tenant configurations with a focus on scalability, modularity, and secure isolation. Its architecture enables seamless integration while maintaining flexibility and efficiency. Key features include:

* **Multi-tenancy :** Only the `Tenant.mappers` data needs adjustments for each Wiremock instance;
* **Promotion Management :** New promotion types can be introduced with ease. Custom promotion rules can be developed and integrated seamlessly, ensuring extensibility without disrupting the current codebase.
* **Database:** PostgreSQL is used as the database for storing tenant, cart, and cart item information.
* **Spring Data JPA:** Spring Data JPA repositories provide a convenient abstraction for database access.  Custom queries are implemented for calculating aggregate values like cart totals.
* **Services:**  Business logic is encapsulated in service classes (`ProductService`, `TenantService`, `CartService`, `CartItemService`, `WiremockService`, `CartSummaryService`).
* **Validation:** Spring Validation is used to validate incoming request parameters.
* **Internationalization:** Error messages are externalized in `messages.properties` files to support multiple languages.
* **Custom Exceptions:** Custom exceptions, such as `CartNotOpenException`, are used to handle specific error conditions and provide more informative error messages.
* **Controller Advice:** A `GlobalExceptionHandler` is implemented to handle exceptions globally and return consistent error responses to the client.


## I. Data model

The system revolves around four key entities:

* **Product:** Represents a product available. Key attributes include:
    * `id`: Unique identifier for the product.
    * `name`: The name of the product.
    * `price`: The price of the product.
    * `promotions`: list of promotions.

* **Promotion:** Represents a discount or offer applicable to cart items.  Attributes:
    * `id`: Unique identifier for the promotion.
    * `type`: The type of promotion (e.g., BUY_X_GET_Y_FREE, FLAT_PERCENT).

* **Cart:** Represents a shopping cart.  Key attributes include:
    * `id`: Unique identifier for the cart.
    * `status`:  Indicates the cart's current state (OPEN, CHECKOUT, CANCELED).
    * `tenantId`:  Indicates the tenant to which the cart belongs.
    * `items`: A collection of `CartItem` objects representing the products in the cart.
    * `itemCount`: The total number of items in the cart.

* **CartItem:**  Represents a specific product within a cart. Attributes include:
    * `id`: Unique identifier for the cart item.
    * `cartId`:  The ID of the cart to which the item belongs.
    * `productId`: The ID of the product.
    * `quantity`: The number of units of the product in the cart.

* **CheckoutCartItem:**  Represents a specific product within a cart. Attributes include:
    * `id`: Unique identifier for the checkout cart item.
    * `productId`: The ID of the product.
    * `productName`: The name of the product associated.
    * `quantity`: The number of units of the product in the cart.
    * `total`: The total price of the items cart.
    * `totalSavings`: The total of discount of the items cart.
    * `totalFinalPrice`: The total price of the items cart with discount.
    * `unitPrice`: The individual price of the product.

* **CartSummary** Represents a specific summary of a cart. Attributes include:
    * `id`: Unique identifier for the cart item.
    * `cartId`:  The ID of the cart to which the item belongs.
    * `total`: The total price of the items cart.
    * `items`: Represents a list of `CheckoutCartItem`
    * `priceSummary`: Represents a specific `Savings` objects representing the cart savings
    * `checkoutDate`: The local date time of checkout.

* **Tenant** Represents a specific tenant. Attributes include:
    * `id`: Unique identifier for tenant.
    * `name`: The name of tenant.
    * `baseUrl`: The url of tenant-service/api 
    * `mappers` the mappers of Product and Promotion that should be respected the formatting of the application domain, example: \
    `"mappers": "{ \"product\": { \"id\": \"productId\", \"name\": \"productName\", \"price\": \"price\" }, \"promotion\": { \"id\": \"promotionId\", \"type\": \"promotionType\", \"amount\": \"amount\", \"required_qty\": \"required_qty\", \"price\": \"price\", \"free_qty\": \"free_qty\" } }"`


## II. API Endpoints

The API is organized around these resources:
* **In the header of your requests in `/carts` and `/cartItems`, add the X-TENANT-ID with the value of the tenantId created in the `POST /tenants` route.**

* **Carts (`/carts`, `CartController`):**
    * `GET /carts`: Retrieves a list of carts. 
    * `POST /carts`: Creates a new cart.  Optionally accepts an initial list of items.  Returns the newly created cart's ID.
    * `POST /carts/{id}/item`: Adds a new item to an OPEN cart.
    * `POST /carts/{id}/clear`: Removes all items from a cart.
    * `POST /carts/{id}/cancel`: Updates the cart status to CANCELED.
    * `POST /carts/{id}/checkout`: Completes the checkout process. Applies promotions, calculates the final total, and updates the cart status to CHECKOUT.
    * `GET /carts/{id}`: Retrieves a specific cart by ID.  Returns the full cart representation. 
    * `GET /carts/{id}/summary`: Retrieves a specific cart summary by cart ID for an CHECKOUT cart.
    * `GET /carts/{id}/savings`: Calculates and returns the total savings for an OPEN cart based on applied promotions.

* **Cart Items (`/cartItems`, `CartItemController`):**
    * `GET /cartItems`: Retrieves cart items.
    * `GET /cartItems/{id}`: Retrieves a specific cart item by ID.
    * `PATCH /cartItems/{id}`: Updates the `quantity` of a cart item.  Validates that the new quantity is greater than zero (`InvalidCartItemQuantityException`). 
    * `DELETE /cartItems/{id}`: Removes item from an OPEN cart.
    * `POST /cartItems`: Adds a new item to an OPEN cart.  Requires `cartId`, `productId`, and `quantity`.  Validates that the quantity is greater than zero.
* **Tenant (`/tenants`, `TenantController`):**
    * `GET /tenants`: Retrieves tenants
    * `GET /tenants/{id}`: Retrieves a specific tenant by ID.
    * `GET /tenants/{id}/products`: Retrieves a list of products for a specific tenant.
    * `GET /tenants/{id}/products/{productId}`: Retrieves a specific product by ID for a specific tenant.
    * `PUT /tenants/{id}`: Updates the `name`,`baseUrl` or `mappers` of a tenant.  
    * `DELETE /tenants/{id}`: Deletes a tenant.
    * `POST /tenants`: Adds a new tenant.  Requires `name`, `baseUrl`, and `mappers`.  
    `mappers` must respect the formatting of the application domain, wiremock/mappings -> domain example: \
    `"mappers": "{ \"product\": { \"id\": \"productId\", \"name\": \"productName\", \"price\": \"price\" }, \"promotion\": { \"id\": \"promotionId\", \"type\": \"promotionType\", \"amount\": \"amount\", \"required_qty\": \"required_qty\", \"price\": \"price\", \"free_qty\": \"free_qty\" } }"`


## III. Follow up questions
<b>1. How long did you spend on the test? What would you add if you had more time?</b>
<br/>Due to the holiday season, now at the end of December and beginning of January, it took me a little over a week and a half to complete the challenge.

<b>2. What was the most useful feature that was added to the latest version of your chosen language? Please include a snippet of code that
shows how you've used it.</b>
<br/>In the latest version of Java (Java 21).

<b>3. What did you find most difficult?</b>
<br/>Creating a scalable system with good practices, so that adding new tenants was as simple as possible, without changing the source code. **

<b>4. What mechanism did you put in place to track down issues in production on this code? If you didn’t put anything, write down what you could do.</b>
<br/>Currently, Log4j2 is the specific mechanism that has been implemented to track issues in production. However, it was only used in TenantInterceptor.

<b>5. Explain your interpretation of the list of requirements and what was delivered or not delivered and why.</b>
<br/> In my opinion, an API should be created to calculate product discounts and facilitate the process of finalizing a purchase in sales system applications. As for tenants, I interpreted that products and their promotions should be consulted in other services. All the requirements have been completed.
