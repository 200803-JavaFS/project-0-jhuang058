DROP TABLE IF EXISTS ownership CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS users CASCADE;



CREATE TABLE users (
	username VARCHAR(30) PRIMARY KEY,
	pw VARCHAR(30) NOT NULL,
	first_name VARCHAR(30) NOT NULL,
	last_name VARCHAR(30) NOT NULL,
	dob DATE,
	user_type int NOT NULL
);

CREATE TABLE accounts (
	account_number SERIAL PRIMARY KEY,
	account_type VARCHAR(30) NOT NULL,
	account_balance NUMERIC(12,2) NOT NULL,
	account_status VARCHAR(30) NOT NULL
);

CREATE TABLE ownership (
	username_fk VARCHAR(30) REFERENCES users(username),
	account_number_fk int REFERENCES accounts(account_number),
	UNIQUE(username_fk, account_number_fk)
);



ALTER SEQUENCE accounts_account_number_seq RESTART WITH 100000000 INCREMENT BY 1;



INSERT INTO users (username, pw, first_name, last_name, dob, user_type)
	VALUES ('c1', '123456', 'Kanye', 'West', '1977-06-08', 1),
			('e1', '123456', 'Lily', 'Li', '1972-07-27', 2),
			('a1', '123456', 'Harry', 'Potter', '1980-03-28', 3);

INSERT INTO accounts (account_type, account_balance, account_status)
	VALUES ('Savings', 3000.00, 'pending');



INSERT INTO ownership (username_fk, account_number_fk)
	VALUES ('c1', 100000000);

