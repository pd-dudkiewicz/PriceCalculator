CREATE SCHEMA pricecalculator;
CREATE TABLE pricecalculator.products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    base_price NUMERIC(8,2) CHECK(base_price > 0),
    name VARCHAR(64)
);
CREATE TABLE pricecalculator.discounts (
    id SERIAL PRIMARY KEY,
    product_id UUID NOT NULL,
    minimum_count INTEGER NOT NULL CHECK(minimum_count > 0),
    percentage SMALLINT CHECK(percentage BETWEEN 1 AND 99),
    FOREIGN KEY (product_id) REFERENCES pricecalculator.products(id)
);