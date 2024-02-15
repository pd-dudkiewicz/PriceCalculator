# PriceCalculator

App for calculating a given product's price based on the number of products ordered.

## Discounts

- Discounts for products can be created, read, updated and deleted via REST API.
- Discounts are applied when number of product ordered is greater than or equal to minimum count set in the discount.
- If the discount should be applied regardless of number of products ordered, then the minimum count should be set to 1.
- Discount percentage needs to be integer greater than 0 and less than 100.
- At any given moment only one discount can be applied to a given product.

## Products

# How to run

Prerequisites:
- [Docker](https://www.docker.com/)

To build docker image and start the app run the following command:

`docker build -t pricecalculator . && docker compose up -d`

The service runs on http://localhost:8080 and API documentation is available on Swagger UI: http://localhost:8080/swagger-ui/index.html
