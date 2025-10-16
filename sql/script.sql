CREATE TABLE clients (
    client_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email_address VARCHAR(150) UNIQUE NOT NULL
);

CREATE TABLE bank_accounts (
    account_id SERIAL PRIMARY KEY,
    account_number VARCHAR(50) UNIQUE NOT NULL,
    current_balance DECIMAL(15, 2) DEFAULT 0.00 NOT NULL,
    owner_id INTEGER NOT NULL,
    account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('CHECKING', 'SAVINGS')),
    overdraft_limit DECIMAL(15, 2) DEFAULT 0.00,
    interest_rate DECIMAL(5, 2) DEFAULT 0.00,
    CONSTRAINT fk_account_owner FOREIGN KEY (owner_id)
        REFERENCES clients(client_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE account_transactions (
    transaction_id SERIAL PRIMARY KEY,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    transaction_amount DECIMAL(15, 2) NOT NULL CHECK (transaction_amount > 0),
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')),
    transaction_location VARCHAR(100),
    account_id INTEGER NOT NULL,
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id)
        REFERENCES bank_accounts(account_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
