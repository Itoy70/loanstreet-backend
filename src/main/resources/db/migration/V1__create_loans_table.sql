CREATE TABLE IF NOT EXISTS loans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount NUMERIC(19, 4) NOT NULL,
    interest_rate NUMERIC(10, 6) NOT NULL,
    length_in_months INTEGER NOT NULL,
    monthly_payment_amount NUMERIC(19, 4) NOT NULL
);
