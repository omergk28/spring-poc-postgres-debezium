CREATE TABLE ${schema_name}.order (
    id uuid PRIMARY KEY,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_address jsonb NULL,
    status VARCHAR(255) NOT NULL
);
