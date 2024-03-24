--CREATE TABLE overdraft_interest

CREATE TABLE IF NOT EXISTS overdraft_interest (
    overdraft_interest_id SERIAL PRIMARY KEY,
    interest_rate_first_days DOUBLE PRECISION NOT NULL,
    interest_rate_after_days DOUBLE PRECISION NOT NULL,
    modification_date DATE NOT NULL
);

--INSERT overdraft_interest

INSERT INTO overdraft_interest (interest_rate_first_days, interest_rate_after_days, modification_date)
VALUES (0.03, 0.06, '2023-09-20');

INSERT INTO overdraft_interest (interest_rate_first_days, interest_rate_after_days, modification_date)
VALUES (0.04, 0.07, '2023-11-05');

INSERT INTO overdraft_interest (interest_rate_first_days, interest_rate_after_days, modification_date)
VALUES (0.02, 0.05, '2023-08-10');

INSERT INTO overdraft_interest (interest_rate_first_days, interest_rate_after_days, modification_date)
VALUES (0.06, 0.09, '2023-12-15');

INSERT INTO overdraft_interest (interest_rate_first_days, interest_rate_after_days, modification_date)
VALUES (0.03, 0.05, '2023-07-25');
