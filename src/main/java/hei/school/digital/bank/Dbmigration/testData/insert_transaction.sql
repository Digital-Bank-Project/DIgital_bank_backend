INSERT INTO transaction (motive, amount, registration_date, transaction_type, account_id)
VALUES
    ('Purchase of groceries', 50.75, CURRENT_TIMESTAMP, 'Debit', 1),
    ('Salary deposit', 1500.00, '2024-03-15 09:00:00', 'Credit', 2),
    ('Electricity bill payment', 80.50, '2024-03-10 15:30:00', 'Debit', 1),
    ('Online shopping', 120.30, '2024-03-14 18:45:00', 'Debit', 3),
    ('Interest received', 25.00, '2024-03-12 12:00:00', 'Credit', 2);
