
CREATE TABLE IF NOT EXISTS balance_history (
    balance_history_id SERIAL PRIMARY KEY,
    account_id INT NOT NULL,
    principal_balance FLOAT NOT NULL,
    laon_amount FLOAT8 NOT NULL,
    interest_amount FLOAT NOT NULL,
    date TIMESTAMP NOT NULL,
);
