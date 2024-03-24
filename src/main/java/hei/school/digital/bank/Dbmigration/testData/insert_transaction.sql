INSERT INTO transfer (id_sender_account, id_receiver_account, transfer_amount, reason, effective_date, registration_date, status, label, account_id)
VALUES
    ('123456', '789012', 500.00, 'Rent payment', '2024-03-01 08:00:00', '2024-03-15 12:00:00', 'Completed', 'Rent', 1),
    ('456789', '012345', 250.00, 'Loan repayment', '2024-03-03 14:30:00', '2024-03-15 12:00:00', 'Completed', 'Loan', 2),
    ('789012', '234567', 100.00, 'Gift to friend', '2024-03-05 10:45:00', '2024-03-15 12:00:00', 'Completed', 'Gift', 3),
    ('012345', '345678', 75.00, 'Utility bill payment', '2024-03-07 16:00:00', '2024-03-15 12:00:00', 'Pending', 'Utility', 2),
    ('234567', '456789', 200.00, 'Dinner expense', '2024-03-10 19:30:00', '2024-03-15 12:00:00', 'Pending', 'Dinner', 1);
