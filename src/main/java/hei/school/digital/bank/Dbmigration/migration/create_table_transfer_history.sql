
CREATE TABLE IF NOT EXISTS transfer_history (
    transfer_history_id SERIAL PRIMARY KEY,
    transfer_id INT NOT NULL,
    actual_amount FLOAT NOT NULL,
);
