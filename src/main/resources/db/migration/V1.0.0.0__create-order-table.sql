CREATE TABLE ${schema_name}.order (
    id uuid PRIMARY KEY,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_address jsonb NULL,
    status VARCHAR(255) NOT NULL
);


INSERT INTO ${schema_name}.order (id, customer_name, customer_email, customer_address, status) VALUES ('6d2c4d98-5aa6-4340-80d8-d737cadea2c6', 'John Doe', 'jd@email.com', '{
  "street": "1234 Elm St",
  "city": "Springfield",
  "state": "IL",
  "zip": "62701"
}' , 'VALIDATED');