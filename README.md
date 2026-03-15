# Mini E-commerce Backend API

A simple RESTful API for managing products and carts.  
The API is implemented using Spring Boot and follows a response format similar to DummyJSON.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

---

## API Overview

| Method | Endpoint | Description |
|------|------|------|
| GET | /products | Get all products |
| GET | /products/{id} | Get product by id |
| GET | /carts/user/{userId} | Get cart by user id |
| POST | /carts | Create cart |
| DELETE | /carts/items/{itemId} | Delete item from cart

---

## How to Run

Clone the repository:

```bash
git clone https://github.com/your-username/mini-ecommerce.git
cd mini-ecommerce
```
Run the application:
```
mvn spring-boot:run
```

Server will start at:
```
http://localhost:8080
```
## API Endpoints
### Products
#### Get all products
```
GET /products
```
##### Example:
```
GET http://localhost:8080/products
```
##### Response example:
```
{
  "products": [
    "id": 1,
    "title": "Essence Mascara Lash Princess",
    "description": "The Essence Mascara Lash Princess is a popular mascara known for its volumizing and lengthening effects. Achieve dramatic lashes with this long-lasting and cruelty-free formula.",
    "price": 9.99,
    "stock": 99,
    "images": [
    "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/1.webp"
    ],
    "thumbnail": "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/thumbnail.webp"
    ],
    "total": 12,
    "skip": 0,
    "limit": 12
}
```
#### Get product by id
```
GET /products/{id}
```
##### Example:
```
GET http://localhost:8080/products/1
```


### Carts

#### Get cart by user id
```
GET /carts/user/{userId}
```
##### Example:
```
GET http://localhost:8080/carts/user/1
```

#### Add products to cart
```
POST /carts
```
##### Request body:
```
{
  "userId": 1,
  "products": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

#### Delete item from cart
```
DELETE /carts/items/{itemId}
```
##### Example:
```
DELETE http://localhost:8080/carts/items/3
```

