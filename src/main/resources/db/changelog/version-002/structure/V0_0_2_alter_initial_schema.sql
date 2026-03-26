ALTER TABLE booking DROP INDEX uniq_seat_flight;
ALTER TABLE user_
  ADD COLUMN stripe_customer_id VARCHAR(50) UNIQUE,
  ADD COLUMN activation_token VARCHAR(255),
  ADD COLUMN activation_token_expiration DATETIME NULL;
