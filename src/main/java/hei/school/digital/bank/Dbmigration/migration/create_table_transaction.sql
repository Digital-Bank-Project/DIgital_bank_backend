
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id SERIAL PRIMARY KEY,
    motive VARCHAR(200) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    account_id INT NOT NULL,
);