--CREATE TABLE TRANSACTION

CREATE TABLE IF NOT EXISTS transaction (
    transaction_id SERIAL PRIMARY KEY,
    transaction_type VARCHAR(50) NOT NULL,
    transaction_amount DOUBLE PRECISION NOT NULL,
    reason VARCHAR(200) NOT NULL,
    effective_date TIMESTAMP NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    account_id INT NOT NULL
);

--INSERT TRANSACTION

INSERT INTO transaction (transaction_type, transaction_amount, reason, effective_date, registration_date, account_id)
VALUES ('Deposit', 1500.00, 'Salary payment', '2024-03-17 08:30:00', '2024-03-17 08:30:00', 1);

INSERT INTO transaction (transaction_type, transaction_amount, reason, effective_date, registration_date, account_id)
VALUES ('Withdrawal', 100.00, 'Grocery shopping', '2024-03-17 12:15:00', '2024-03-17 12:15:00', 2);

INSERT INTO transaction (transaction_type, transaction_amount, reason, effective_date, registration_date, account_id)
VALUES ('Transfer', 200.00, 'Friend repayment', '2024-03-17 15:45:00', '2024-03-17 15:45:00', 3);

INSERT INTO transaction (transaction_type, transaction_amount, reason, effective_date, registration_date, account_id)
VALUES ('Withdrawal', 50.00, 'Coffee shop', '2024-03-17 18:20:00', '2024-03-17 18:20:00', 4);

INSERT INTO transaction (transaction_type, transaction_amount, reason, effective_date, registration_date, account_id)
VALUES ('Deposit', 300.00, 'Bonus payment', '2024-03-17 21:00:00', '2024-03-17 21:00:00', 5);


