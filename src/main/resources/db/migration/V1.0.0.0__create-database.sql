CREATE SCHEMA orderdb;

CREATE TABLE orderdb.order (
    id uuid PRIMARY KEY,
    order_date TIMESTAMP NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_address VARCHAR(255) NOT NULL,
    order_status VARCHAR(255) NOT NULL
);