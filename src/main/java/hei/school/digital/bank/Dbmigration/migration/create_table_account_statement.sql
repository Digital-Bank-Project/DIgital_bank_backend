
CREATE TABLE IF NOT EXISTS account_statement (
    account_statement_id SERIAL PRIMARY KEY,
    account_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    motive VARCHAR(200) NOT NULL,
    balance_principal FLOAT NOT NULL,
    credit_mga FLOAT8 NOT NULL,
    debit_mga FLOAT8 NOT NULL,
);
