
CREATE TABLE IF NOT EXISTS transfer (
    transfer_id SERIAL PRIMARY KEY,
    id_sender_account VARCHAR(50) NOT NULL,
    id_receiver_account VARCHAR(50) NOT NULL,
    transfer_amount DOUBLE PRECISION NOT NULL,
    reason VARCHAR(200) NOT NULL,
    effective_date TIMESTAMP NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    label VARCHAR(100) NOT NULL,
    account_id INT NOT NULL
);
