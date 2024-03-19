--CREATE TABLE TRANSFERT

User
CREATE TABLE IF NOT EXISTS transfer (
    transfer_id SERIAL PRIMARY KEY,
    id_sender_account VARCHAR(50) NOT NULL,
    id_receiver_account VARCHAR(50) NOT NULL,
    transfer_amount DOUBLE PRECISION NOT NULL,
    reason VARCHAR(200) NOT NULL,
    effective_date TIMESTAMP NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    reference VARCHAR(100) NOT NULL,
    label VARCHAR(100) NOT NULL,
    account_id INT NOT NULL
);

--INSERT TRANSFERT

INSERT INTO transfer (id_sender_account, id_receiver_account, transfer_amount, reason, effective_date, registration_date, status, reference, label, account_id)
VALUES ('ABC123', 'XYZ789', 500.00, 'Rent payment', '2024-03-17 10:00:00', '2024-03-17 10:00:00', 'Completed', 'REF123', 'Rent', 1);

INSERT INTO transfer (id_sender_account, id_receiver_account, transfer_amount, reason, effective_date, registration_date, status, reference, label, account_id)
VALUES ('DEF456', 'GHI789', 300.00, 'Loan repayment', '2024-03-17 13:30:00', '2024-03-17 13:30:00', 'Completed', 'REF456', 'Loan', 2);

INSERT INTO transfer (id_sender_account, id_receiver_account, transfer_amount, reason, effective_date, registration_date, status, reference, label, account_id)
VALUES ('JKL321', 'MNO987', 200.00, 'Gift', '2024-03-17 16:45:00', '2024-03-17 16:45:00', 'Completed', 'REF789', 'Birthday', 3);

INSERT INTO transfer (id_sender_account, id_receiver_account, transfer_amount, reason, effective_date, registration_date, status, reference, label, account_id)
VALUES ('PQR654', 'STU321', 150.00, 'Charity donation', '2024-03-17 19:20:00', '2024-03-17 19:20:00', 'Completed', 'REFABC', 'Donation', 4);

INSERT INTO transfer (id_sender_account, id_receiver_account, transfer_amount, reason, effective_date, registration_date, status, reference, label, account_id)
VALUES ('VWX987', 'YZA456', 400.00, 'Investment', '2024-03-17 22:00:00', '2024-03-17 22:00:00', 'Completed', 'REFXYZ', 'Stocks', 5);


