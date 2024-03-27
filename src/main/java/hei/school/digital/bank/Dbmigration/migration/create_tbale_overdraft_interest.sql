
CREATE TABLE IF NOT EXISTS overdraft_interest (
    overdraft_interest_id SERIAL PRIMARY KEY,
    interest_rate_first_days DOUBLE PRECISION NOT NULL,
    interest_rate_after_days DOUBLE PRECISION NOT NULL
);
