
CREATE TABLE IF NOT EXISTS account (
    account_id SERIAL PRIMARY KEY,
    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    date_of_birth DATE CHECK (date_of_birth <= CURRENT_DATE - INTERVAL '23 years'),
    monthly_salary_net DOUBLE PRECISION NOT NULL,
    unique_account_number VARCHAR(10) NOT NULL,
    overdraft_enabled BOOL NOT NULL,
    bank VARCHAR(100) NOT NULL,
    overdraft_interest_details FLOAT8 NOT NULL,
    overdraft_start_date TIMESTAMP NOT NULL,
    overdraft_interest_id INT NOT NULL
);