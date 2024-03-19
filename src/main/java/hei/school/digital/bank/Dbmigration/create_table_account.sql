--CREATE TABLE ACCOUNT

CREATE TABLE IF NOT EXISTS account (
    account_id SERIAL PRIMARY KEY,
    last_name VARCHAR(150),
    first_name VARCHAR(150),
    date_of_birth DATE CHECK (date_of_birth <= CURRENT_DATE - INTERVAL '23 years'),
    monthly_net_salary DOUBLE PRECISION,
    unique_account_number VARCHAR(20),
    overdraft_status BOOLEAN
);
-- INSERT ACCOUNT

INSERT INTO account (last_name, first_name, date_of_birth, monthly_net_salary, unique_account_number, overdraft_status)
VALUES ('Doe', 'John', '2000-01-15', 2500.00, 'ABC123', TRUE);

INSERT INTO account (last_name, first_name, date_of_birth, monthly_net_salary, unique_account_number, overdraft_status)
VALUES ('Smith', 'Alice', '1995-05-20', 3200.00, 'XYZ789', FALSE);

INSERT INTO account (last_name, first_name, date_of_birth, monthly_net_salary, unique_account_number, overdraft_status)
VALUES ('Johnson', 'Michael', '1992-11-10', 2800.00, 'DEF456', TRUE);

INSERT INTO account (last_name, first_name, date_of_birth, monthly_net_salary, unique_account_number, overdraft_status)
VALUES ('Brown', 'Emma', '1998-08-03', 2000.00, 'GHI789', FALSE);

INSERT INTO account (last_name, first_name, date_of_birth, monthly_net_salary, unique_account_number, overdraft_status)
VALUES ('Wilson', 'Sophia', '1990-04-25', 3500.00, 'JKL321', TRUE);

